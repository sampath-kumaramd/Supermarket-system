package com.ead.inventorymanagerservice.Controller;


import com.ead.inventorymanagerservice.Entity.Customer;
import com.ead.inventorymanagerservice.Exceptions.UserNotFoundException;
import com.ead.inventorymanagerservice.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/getAll")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/getById/{userId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer userId) {
        try {
            Customer customer = customerService.getCustomerById(userId);
            return ResponseEntity.ok(customer);
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    @PutMapping("/updateDetails/{userId}")
    public ResponseEntity<String> updateCustomer(@RequestBody Customer customer,@PathVariable Integer userId){
        return customerService.updateCustomer(customer,userId);
    }

    @DeleteMapping("/deleteById/{userId}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Integer userId) {
        return customerService.deleteCustomerById(userId);
    }


}
