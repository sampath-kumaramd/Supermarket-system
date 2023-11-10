package com.example.Product.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;


    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;
}


