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
public class CartLineItemsDto {
    private Long id;
    private String skuCode;
    private int quantity;
    private BigDecimal price;
}
