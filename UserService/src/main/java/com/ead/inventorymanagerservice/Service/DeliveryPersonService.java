package com.ead.inventorymanagerservice.Service;


import com.ead.inventorymanagerservice.Entity.DeliveryPerson;
import com.ead.inventorymanagerservice.Repository.DeliveryPersonRepository;
import com.ead.inventorymanagerservice.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class DeliveryPersonService {

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;


public int generateDeliveryPersonId() {
    DeliveryPerson deliveryPerson = deliveryPersonRepository.findFirstByOrderByDeliveryPersonIdDesc();
    if (deliveryPerson != null) {
        int lastDeliveryPersonId = deliveryPerson.getDeliveryPersonId();
        return lastDeliveryPersonId + 1;
    } else {
        return 1;
    }
}

    public ResponseEntity<?> getAllDeliveryPerson() {
        return deliveryPersonRepository.findAll().isEmpty() ? ResponseEntity.ok("No Delivery Person Found") : ResponseEntity.ok(deliveryPersonRepository.findAll());
    }

    public DeliveryPerson getDeliverPersonById(int deliverPersonId){
        return deliveryPersonRepository.getDeliveryPersonByDeliveryPersonId(deliverPersonId);
    }

    public ResponseEntity<String> saveDeliveryPerson(DeliveryPerson deliveryPerson) {
        try {
            DeliveryPerson existingCustomer = deliveryPersonRepository.getDeliveryPersonByEmail(deliveryPerson.getEmail());
//            deliveryPerson.setDeliveryPersonId(generateDeliveryPersonId());
            if (existingCustomer != null) {
                throw new UserAlreadyExistsException("Email: " + deliveryPerson.getEmail() + " is already exist");
            }
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> response = restTemplate.getForEntity(
                    "http://localhost:8083/auth/checkEmailExists/{email}",
                    String.class,
                    deliveryPerson.getEmail()
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                String requestJson = "{" +
                        "\"userId\":\"" + deliveryPerson.getDeliveryPersonId() + "\"," +
                        "\"firstName\":\"" + deliveryPerson.getFirstName() + "\"," +
                        "\"lastName\":\"" + deliveryPerson.getLastName() + "\"," +
                        "\"email\":\"" + deliveryPerson.getEmail() + "\"," +
                        "\"password\":\"" + deliveryPerson.getPassword() + "\"," +
                        "\"role\":\"DELIVERY_PERSON\"" +
                        "}";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
                ResponseEntity<String> registerResponse = restTemplate.postForEntity(
                        "http://localhost:8083/auth/register",
                        requestEntity,
                        String.class
                );

                if (registerResponse.getStatusCode() == HttpStatus.OK) {
                    deliveryPersonRepository.save(deliveryPerson);
                    return ResponseEntity.ok("Delivery Person saved successfully");
                } else {
                    throw new UserAlreadyExistsException("Error registering user");
                }
            } else {
                throw new UserAlreadyExistsException("Email: " + deliveryPerson.getEmail() + " is already exist");
            }
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body("Email: " + deliveryPerson.getEmail() + " is already exist");
        }
    }

    public ResponseEntity<?> updateDeliveryPerson(int deliverPersonId,DeliveryPerson deliveryPerson){
        DeliveryPerson updatedDeliverPerson = deliveryPersonRepository.getDeliveryPersonByDeliveryPersonId(deliverPersonId);
        if(updatedDeliverPerson == null){
            return ResponseEntity.badRequest().body("No Delivery Person Found");
        }
        updatedDeliverPerson.setFirstName(deliveryPerson.getFirstName());
        updatedDeliverPerson.setLastName(deliveryPerson.getLastName());
        updatedDeliverPerson.setContactNumber(deliveryPerson.getContactNumber());
        updatedDeliverPerson.setAddress(deliveryPerson.getAddress());
        return ResponseEntity.ok(deliveryPersonRepository.save(updatedDeliverPerson));
    }
}
