package com.ead.inventorymanagerservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data

@AllArgsConstructor
@NoArgsConstructor

public class InventoryManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inventoryManagerId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String address;
    private String password;
}


