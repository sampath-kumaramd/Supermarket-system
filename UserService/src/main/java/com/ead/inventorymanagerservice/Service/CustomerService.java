package com.ead.inventorymanagerservice.Service;


import com.ead.inventorymanagerservice.Entity.Customer;
import com.ead.inventorymanagerservice.Exceptions.UserAlreadyExistsException;
import com.ead.inventorymanagerservice.Exceptions.UserNotFoundException;
import com.ead.inventorymanagerservice.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    RestTemplate restTemplate = new RestTemplate();


//    public int generateUserId() {
//        Customer lastCustomer = customerRepository.findFirstByOrderByUserIdDesc();
//        if (lastCustomer != null) {
//            int lastUserId = Integer.parseInt(lastCustomer.getUserId().substring(1));
//            int newUserId = lastUserId + 1;
//            return "U" + newUserId;
//        } else {
//            return "U1";
//        }
//    }



    //////////dontknow----code
public int generateUserId() {
    Customer lastCustomer = customerRepository.findFirstByOrderByUserIdDesc();
    if (lastCustomer != null) {
        int lastUserId = lastCustomer.getUserId();

        // Convert int to String
        String lastUserIdStr = String.valueOf(lastUserId);

        // Extract numeric part using regex
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(lastUserIdStr);

        if (matcher.find()) {
            int lastNumericUserId = Integer.parseInt(matcher.group());
            return lastNumericUserId + 1;
        } else {
            return 1;
        }
    } else {
        return 1;
    }
}





    public ResponseEntity<String> saveCustomer(Customer customer) {
        try {
            Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());
//            customer.setUserId(generateUserId());
            if (existingCustomer != null) {
                throw new UserAlreadyExistsException("Email: " + customer.getEmail() + " is already exist");
            }
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> response = restTemplate.getForEntity(

                    "http://localhost:8083/auth/checkEmailExists/{email}",
                    String.class,
                    customer.getEmail()
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                String requestJson = "{" +
                        "\"userId\":\"" + customer.getUserId() + "\"," +
                        "\"firstName\":\"" + customer.getFirstName() + "\"," +
                        "\"lastName\":\"" + customer.getLastName() + "\"," +
                        "\"email\":\"" + customer.getEmail() + "\"," +
                        "\"password\":\"" + customer.getPassword() + "\"," +
                        "\"role\":\"CUSTOMER\"" +
                        "}";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
                ResponseEntity<String> registerResponse = restTemplate.postForEntity(
                        "http://localhost:8083//auth/register",
                        requestEntity,
                        String.class
                );

                if (registerResponse.getStatusCode() == HttpStatus.OK) {
//                    customer.setOrderStatus(Customer.OrderStatus.NOT_APPLICABLE);
                    customerRepository.save(customer);
                    return ResponseEntity.ok("Customer saved successfully");
                } else {
                    throw new UserAlreadyExistsException("Error registering user");
                }
            } else {
                throw new UserAlreadyExistsException("Email: " + customer.getEmail() + " is already exist");
            }
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body("Email: " + customer.getEmail() + " is already exist");
        }
    }

    public ResponseEntity<String> updateCustomer(Customer customer, Integer userId) {
        try {
            Customer existingCustomer = customerRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setContactNumber(customer.getContactNumber());
            existingCustomer.setAddress(customer.getAddress());
            customerRepository.save(existingCustomer);
            return ResponseEntity.ok("Customer updated successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Integer userId) throws UserNotFoundException {
        return customerRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public ResponseEntity<String> deleteCustomerById(Integer userId) {
        try {
            Customer customer = customerRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
            customerRepository.delete(customer);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
