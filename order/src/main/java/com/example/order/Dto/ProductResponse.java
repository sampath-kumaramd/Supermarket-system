package com.example.order.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {


    private Long id;
    private String skuCode;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
    private boolean isInStock;
    private String productCategory;

}
