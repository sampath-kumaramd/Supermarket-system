package com.ead.inventorymanagerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class InventoryManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagerServiceApplication.class, args);
    }

}
