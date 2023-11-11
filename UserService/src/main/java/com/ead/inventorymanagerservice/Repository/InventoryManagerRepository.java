package com.ead.inventorymanagerservice.Repository;

import com.ead.inventorymanagerservice.Entity.InventoryManager;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryManagerRepository extends JpaRepository<InventoryManager,String> {
    Optional<InventoryManager> findInventoryManagerByInventoryManagerId(Integer inventoryManagerId);
    InventoryManager findFirstByOrderByInventoryManagerIdDesc();
    InventoryManager findInventoryManagerByEmail(String email);

}
