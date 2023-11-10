package com.example.Product.repository;

import com.example.Product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findBySkuCodeIn(List<String> skuCode);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByNameContaining(String name);

    @Query("SELECT p FROM Product p WHERE p.skuCode = :skuCode")
    Product findBySkuCode(@Param("skuCode") String skuCode);

}
