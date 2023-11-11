package com.ead.inventorymanagerservice.Controller;

import com.ead.inventorymanagerservice.Entity.Admin;
import com.ead.inventorymanagerservice.Entity.Customer;
import com.ead.inventorymanagerservice.Exceptions.UserNotFoundException;
import com.ead.inventorymanagerservice.Service.AdminService;
import com.ead.inventorymanagerservice.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody Admin admin){
        return adminService.saveAdmin(admin);
    }

//    @GetMapping("/getAll")
//    public List<Admin> getAllAdmins(){
//        return adminService.getAllAdmins();
//    }

    @GetMapping("/getById/{userId}")
    public ResponseEntity<?> getAdminById(@PathVariable Integer userId) {
        try {
            Admin admin = adminService.getAdminById(userId);
            return ResponseEntity.ok(admin);
        } catch (UserNotFoundException e) {
            return null;
        }
    }




}

