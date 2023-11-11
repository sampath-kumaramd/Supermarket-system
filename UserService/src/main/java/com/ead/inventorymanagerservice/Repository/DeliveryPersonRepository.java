package com.ead.inventorymanagerservice.Repository;


import com.ead.inventorymanagerservice.Entity.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson,Integer> {

    DeliveryPerson findFirstByOrderByDeliveryPersonIdDesc();
    DeliveryPerson getDeliveryPersonByDeliveryPersonId(Integer deliveryPersonId);
    DeliveryPerson getDeliveryPersonByEmail(String deliverPersonEmail);

}
