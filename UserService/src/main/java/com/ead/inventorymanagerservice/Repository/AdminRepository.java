package com.ead.inventorymanagerservice.Repository;

import com.ead.inventorymanagerservice.Entity.Admin;
import com.ead.inventorymanagerservice.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {

    Admin findFirstByOrderByUserIdDesc();
    Admin findByEmail(String email);
}
