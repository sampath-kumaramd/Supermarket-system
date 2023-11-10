package com.example.Product.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.Product.dto.ProductCategoryResponse;
import com.example.Product.dto.ProductRequest;
import com.example.Product.dto.ProductResponse;
import com.example.Product.dto.QuantityResponse;
import com.example.Product.exception.ItemNotFoundException;
import com.example.Product.model.Product;
import com.example.Product.model.ProductCategory;
import com.example.Product.repository.ProductCategoryRepository;
import com.example.Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.Product.Service.ProductMapper.mapToProductResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CloudinaryImageUploader imageUploader;

    public void createProductWithCategory(ProductRequest productRequest, Long categoryId) {
        ProductCategory category = productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemNotFoundException("Product Category not found"));

        Product product = ProductCreator.createProduct(productRequest, category);

        // Make sure to set the product in the product category's productList
        category.getProductList().add(product);

        // Set the category in the product
        product.setProductCategory(category);

        productRepository.save(product);
        productCategoryRepository.save(category); // Save the category to update the relationship
    }


    public void uploadImage(Long productId, MultipartFile imageFile) throws IOException {
        Product product = getProductByIdFromRepository(productId);

        String imageUrl = imageUploader.uploadImage(productId, imageFile);
        product.setImageUrl(imageUrl);

        productRepository.save(product);
        log.info("Image URL {} saved for Product {}", imageUrl, product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper::mapToProductResponse).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = getProductByIdFromRepository(id); // Call the correct method to retrieve Product
        return mapToProductResponse(product);
    }

    private Product getProductByIdFromRepository(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Product not found"));
    }


    public ProductResponse getProductBySkuCode(String skuCode) {
        Product product = productRepository.findBySkuCode(skuCode);

        if (product == null) {
            throw new ItemNotFoundException("Product not found for SKU code: " + skuCode);
        }

        return mapToProductResponse(product);
    }

    public List<ProductResponse> searchProductByName(String name) {
        List<Product> product = productRepository.findByNameContaining(name);
        return product.stream()
                .map(ProductMapper::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional()
    @SneakyThrows
    public List<QuantityResponse> isInStock(List<String> skuCodes, List<Integer> userEnteredQuantities) {
        List<Product> products = productRepository.findBySkuCodeIn(skuCodes);
        List<QuantityResponse> responses = new ArrayList<>();

        for (int i = 0; i < skuCodes.size(); i++) {
            String skuCode = skuCodes.get(i);
            Product product = products.stream()
                    .filter(p -> p.getSkuCode().equals(skuCode))
                    .findFirst()
                    .orElse(null);

            if (product != null) {
                int productQuantity = product.getQuantity();
                int userEnteredQuantity = userEnteredQuantities.get(i);
                boolean isInStock = userEnteredQuantity <= productQuantity;

                QuantityResponse response = QuantityResponse.builder()
                        .quantity(productQuantity)
                        .isInStock(isInStock)
                        .build();

                responses.add(response);
            }
        }
        return responses;
    }

    public void updateProduct(Long id, Map<String, Object> productFields) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Product not found"));

        // Update only the provided fields
        if (productFields.containsKey("skuCode")) {
            product.setSkuCode((String) productFields.get("skuCode"));
        }
        if (productFields.containsKey("name")) {
            product.setName((String) productFields.get("name"));
        }
        if (productFields.containsKey("price")) {
            product.setPrice(BigDecimal.valueOf((Double) productFields.get("price")));
        }
        if (productFields.containsKey("quantity")) {
            product.setQuantity((Integer) productFields.get("quantity"));
        }

        productRepository.save(product);
        log.info("Product {} is updated", product.getId());
    }


    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Product not found"));
        productRepository.delete(product);
        log.info("Product {} is deleted", id);
    }

//    public List<ProductResponse> searchProductsByName(String name) {
//        List<Product> products = productRepository.findByNameContaining(name);
//        return products.stream().map(this::mapToProductResponse).toList();
//    }

    @Transactional
    public void addQuantityToProduct(Long id, int quantityToAdd) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Product not found"));
        int updatedQuantity = product.getQuantity() + quantityToAdd;
        product.setQuantity(updatedQuantity);
        productRepository.save(product);
        log.info("Added {} to the quantity of product {}", quantityToAdd, product.getId());
    }

    @Transactional
    public void reduceQuantityOfProduct(Long id, int quantityToReduce) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Product not found"));
        int updatedQuantity = product.getQuantity() - quantityToReduce;
        if (updatedQuantity < 0) {
            throw new ItemNotFoundException("Not enough quantity in stock");
        }
        product.setQuantity(updatedQuantity);
        productRepository.save(product);
        log.info("Reduced {} from the quantity of product {}", quantityToReduce, product.getId());
    }

    @Transactional
    public void addQuantityToProducts(List<String> skuCodes, List<Integer> quantitiesToAdd) {
        List<Product> products = productRepository.findBySkuCodeIn(skuCodes);

        for (int i = 0; i < skuCodes.size(); i++) {
            String skuCode = skuCodes.get(i);
            int quantityToAdd = quantitiesToAdd.get(i);

            Product product = products.stream()
                    .filter(p -> p.getSkuCode().equals(skuCode))
                    .findFirst()
                    .orElseThrow(() -> new ItemNotFoundException("Product not found for SKU code: " + skuCode));

            int updatedQuantity = product.getQuantity() + quantityToAdd;
            product.setQuantity(updatedQuantity);
            productRepository.save(product);

            log.info("Added {} to the quantity of product {}", quantityToAdd, product.getId());
        }
    }


    @Transactional
    public void reduceQuantityOfProducts(List<String> skuCodes, List<Integer> quantitiesToReduce) {
        List<Product> products = productRepository.findBySkuCodeIn(skuCodes);

        for (int i = 0; i < skuCodes.size(); i++) {
            String skuCode = skuCodes.get(i);
            int quantityToReduce = quantitiesToReduce.get(i);

            Product product = products.stream()
                    .filter(p -> p.getSkuCode().equals(skuCode))
                    .findFirst()
                    .orElseThrow(() -> new ItemNotFoundException("Product not found for SKU code: " + skuCode));

            int updatedQuantity = product.getQuantity() - quantityToReduce;

            if (updatedQuantity < 0) {
                throw new ItemNotFoundException("Not enough quantity in stock for SKU code: " + skuCode);
            }

            product.setQuantity(updatedQuantity);
            productRepository.save(product);

            log.info("Reduced {} from the quantity of product {}", quantityToReduce, product.getId());
        }
    }

}
