package com.example.Product.controller;

import com.example.Product.Service.ProductService;
import com.example.Product.dto.ProductRequest;
import com.example.Product.dto.ProductResponse;
import com.example.Product.dto.QuantityRequest;
import com.example.Product.dto.QuantityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create/{categoryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProductWithCategory(@RequestBody ProductRequest productRequest, @PathVariable Long categoryId) {
        productService.createProductWithCategory(productRequest, categoryId);
    }

    @PostMapping("/{id}/upload-image")
    @ResponseStatus(HttpStatus.OK)
    public void uploadProductImage(@PathVariable Long id, @RequestParam("image") MultipartFile imageFile) {
        try {
            productService.uploadImage(id, imageFile);
        } catch (IOException e) {
            // Handle the exception
        }
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> productFields) {
        productService.updateProduct(id, productFields);
    }

    @GetMapping("/sku/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductBySkuCode(@PathVariable String skuCode) {
        return productService.getProductBySkuCode(skuCode);
    }
    @PutMapping("/{id}/add-quantity")
    @ResponseStatus(HttpStatus.OK)
    public void addQuantityToProduct(@PathVariable Long id, @RequestBody Integer quantityToAdd) {
        productService.addQuantityToProduct(id, quantityToAdd);
    }
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProductCategoriesByName(@RequestParam String name) {
        return productService.searchProductByName(name);
    }
    @PutMapping("/add-quantities")
    @ResponseStatus(HttpStatus.OK)
    public void addQuantityToProducts(@RequestBody QuantityRequest request) {
        productService.addQuantityToProducts(request.getSkuCodes(), request.getQuantities());
    }

    @PutMapping("/reduce-quantities")
    @ResponseStatus(HttpStatus.OK)
    public void reduceQuantityOfProducts(@RequestBody QuantityRequest request) {
        productService.reduceQuantityOfProducts(request.getSkuCodes(), request.getQuantities());
    }
    @PutMapping("/{id}/reduce-quantity")
    @ResponseStatus(HttpStatus.OK)
    public void reduceQuantityOfProduct(@PathVariable Long id, @RequestBody int quantityToReduce) {
        productService.reduceQuantityOfProduct(id, quantityToReduce);
    }

    @GetMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public List<QuantityResponse> isInStock(@RequestBody QuantityRequest request ) {
        return productService.isInStock(request.getSkuCodes(), request.getQuantities());
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
