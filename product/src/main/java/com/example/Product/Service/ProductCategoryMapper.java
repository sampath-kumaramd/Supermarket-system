package com.example.Product.Service;

import com.example.Product.dto.ProductCategoryResponse;
import com.example.Product.dto.ProductResponse;
import com.example.Product.model.Product;
import com.example.Product.model.ProductCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductCategoryMapper {

    public ProductCategoryResponse mapToProductCategoryResponse(ProductCategory productCategory) {
        List<ProductResponse> productResponses = productCategory.getProductList().stream()
                .map(ProductMapper::mapToProductResponse)
                .collect(Collectors.toList());

        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .productDtoList(productResponses)
                .build();
    }

    public ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .skuCode(product.getSkuCode())
                .name(product.getName())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .productCategory(product.getProductCategory().getName())
                .build();
    }


    public void updateProductCategoryFields(ProductCategory productCategory, Map<String, Object> productCategoryFields) {
        if (productCategoryFields.containsKey("name")) {
            productCategory.setName((String) productCategoryFields.get("name"));
        }
        // Add more fields to update as needed
    }
}
