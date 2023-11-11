package com.ead.inventorymanagerservice.Service;

import com.ead.inventorymanagerservice.Entity.Admin;
import com.ead.inventorymanagerservice.Entity.Customer;
import com.ead.inventorymanagerservice.Exceptions.UserAlreadyExistsException;
import com.ead.inventorymanagerservice.Exceptions.UserNotFoundException;
import com.ead.inventorymanagerservice.Repository.AdminRepository;
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
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

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
        Admin lastAdmin = adminRepository.findFirstByOrderByUserIdDesc();
        if (lastAdmin != null) {
            int lastUserId = lastAdmin.getUserId();

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





    public ResponseEntity<String> saveAdmin(Admin admin) {
        try {
            Admin existingAdmin = adminRepository.findByEmail(admin.getEmail());
//            admin.setUserId(generateUserId());
            if (existingAdmin != null) {
                throw new UserAlreadyExistsException("Email: " + admin.getEmail() + " is already exist");
            }
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> response = restTemplate.getForEntity(

                    "http://localhost:8083/auth/checkEmailExists/{email}",
                    String.class,
                    admin.getEmail()
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                String requestJson = "{" +
                        "\"userId\":\"" + admin.getUserId() + "\"," +
                        "\"firstName\":\"" + admin.getFirstName() + "\"," +
                        "\"lastName\":\"" + admin.getLastName() + "\"," +
                        "\"email\":\"" + admin.getEmail() + "\"," +
                        "\"password\":\"" + admin.getPassword() + "\"," +
                        "\"role\":\"ADMIN\"" +
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
                    adminRepository.save(admin);
                    return ResponseEntity.ok("Customer saved successfully");
                } else {
                    throw new UserAlreadyExistsException("Error registering user");
                }
            } else {
                throw new UserAlreadyExistsException("Email: " + admin.getEmail() + " is already exist");
            }
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body("Email: " + admin.getEmail() + " is already exist");
        }
    }





    public Admin getAdminById(Integer userId) throws UserNotFoundException {
        return adminRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public ResponseEntity<String> deleteCustomerById(Integer userId) {
        try {
            Admin admin = adminRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
            adminRepository.delete(admin);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}


