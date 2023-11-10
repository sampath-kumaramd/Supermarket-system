package com.example.Product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductCategoryRequest {
    private String name;
    private List<ProductDto> productDtoList;
}
