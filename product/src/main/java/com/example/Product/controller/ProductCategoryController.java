package com.example.Product.controller;

import com.example.Product.Service.ProductCategoryService;
import com.example.Product.dto.ProductCategoryRequest;
import com.example.Product.dto.ProductCategoryResponse;
import com.example.Product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product/product-category")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProductCategory(@RequestBody ProductCategoryRequest productCategory) {
        productCategoryService.createProductCategory(productCategory);
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductCategoryResponse> getAllProductCategories() {
        return productCategoryService.getAllProductCategories();
    }

    // Get a specific product category by ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductCategoryResponse getProductCategoryById(@PathVariable Long id) {
        return productCategoryService.getProductCategoryById(id);
    }

    // Update an existing product category by ID
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProductCategory(
            @PathVariable Long id,
            @RequestBody Map<String , Object> productCategoryFields) {
        productCategoryService.updateProductCategory(id, productCategoryFields);
    }

    // Delete a specific product category by ID
    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<String> deleteProductCategory(@PathVariable Long categoryId) {
        productCategoryService.deleteProductCategory(categoryId);
        return ResponseEntity.ok("Product category deleted successfully.");
    }

    // Search for product categories by name
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductCategoryResponse> searchProductCategoriesByName(@RequestParam String name) {
        return productCategoryService.searchProductCategoriesByName(name);
    }

    @GetMapping("/{categoryId}/products")
    public List<ProductResponse> getProductsByCategoryId(@PathVariable Long categoryId) {
       return productCategoryService.getProductsByCategoryId(categoryId);
    }

}
