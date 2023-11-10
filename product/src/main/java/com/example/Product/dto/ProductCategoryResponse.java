package com.example.Product.dto;

import com.example.Product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductCategoryResponse {
    private Long id;
    private String name;
    private List<ProductResponse> productDtoList;
}
