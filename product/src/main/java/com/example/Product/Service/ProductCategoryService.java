package com.example.Product.Service;

import com.example.Product.Service.ProductCategoryMapper;
import com.example.Product.Service.ProductMapper;
import com.example.Product.repository.ProductCategoryRepository;
import com.example.Product.dto.ProductCategoryRequest;
import com.example.Product.dto.ProductCategoryResponse;
import com.example.Product.dto.ProductResponse;
import com.example.Product.exception.ItemNotFoundException;
import com.example.Product.model.Product;
import com.example.Product.model.ProductCategory;
import com.example.Product.repository.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductMapper productMapper;

    public void createProductCategory(ProductCategoryRequest productCategoryRequest) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(productCategoryRequest.getName());
        productCategoryRepository.save(productCategory);
    }

    public List<ProductCategoryResponse> getAllProductCategories() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        return productCategories.stream()
                .map(productCategoryMapper::mapToProductCategoryResponse)
                .collect(Collectors.toList());
    }

    public ProductCategoryResponse getProductCategoryById(Long id) {
        ProductCategory productCategory = getProductCategory(id);
        return productCategoryMapper.mapToProductCategoryResponse(productCategory);
    }

    public void updateProductCategory(Long id, Map<String, Object> productCategoryFields) {
        ProductCategory productCategory = getProductCategory(id);
        productCategoryMapper.updateProductCategoryFields(productCategory, productCategoryFields);
        productCategoryRepository.save(productCategory);
    }

    public List<ProductCategoryResponse> searchProductCategoriesByName(String name) {
        List<ProductCategory> productCategories = productCategoryRepository.findByNameContaining(name);
        return productCategories.stream()
                .map(productCategoryMapper::mapToProductCategoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProductCategory(Long id) {
        ProductCategory productCategory = getProductCategory(id);
        productCategoryRepository.delete(productCategory);
    }

    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productCategoryRepository.findProductsByCategoryId(categoryId);
        return products.stream()
                .map(productCategoryMapper::mapToProductResponse)  // Use the correct mapper reference
                .collect(Collectors.toList());
    }


    private ProductCategory getProductCategory(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Product Category not found"));
    }
}
