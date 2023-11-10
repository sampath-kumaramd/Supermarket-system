package com.example.delivery.Repository;

import com.example.delivery.Entity.Delivery;
import com.example.delivery.Entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress,Long> {
}
