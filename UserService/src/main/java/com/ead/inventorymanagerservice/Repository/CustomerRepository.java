package com.ead.inventorymanagerservice.Repository;

import com.ead.inventorymanagerservice.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Customer findFirstByOrderByUserIdDesc();
    Customer findByEmail(String email);
}
