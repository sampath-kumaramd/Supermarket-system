package com.example.Product.repository;

import com.example.Product.model.Product;
import com.example.Product.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> {
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.name LIKE %:name%")
    List<ProductCategory> findByNameContaining(String name);

    @Query("SELECT p FROM Product p WHERE p.productCategory.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);

}
