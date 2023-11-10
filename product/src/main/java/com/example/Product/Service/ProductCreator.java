package com.example.Product.Service;

import com.example.Product.dto.ProductRequest;
import com.example.Product.model.Product;
import com.example.Product.model.ProductCategory;

import java.util.UUID;

public class ProductCreator {

    public static Product createProduct(ProductRequest productRequest, ProductCategory category) {
        Product product = new Product();
        product.setSkuCode(UUID.randomUUID().toString());
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setProductCategory(category);
        return product;
    }
}
