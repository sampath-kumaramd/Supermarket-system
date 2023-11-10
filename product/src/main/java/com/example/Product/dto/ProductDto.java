package com.example.Product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductDto {
    private Long id;
    private String skuCode;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
}
