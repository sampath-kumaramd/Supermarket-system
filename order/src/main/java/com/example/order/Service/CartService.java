package com.example.order.Service;

import com.example.order.Dto.CartLineItemsDto;
import com.example.order.Dto.CartRequest;
import com.example.order.Dto.CartResponse;
import com.example.order.Dto.ProductResponse;
import com.example.order.Model.Cart;
import com.example.order.Model.CartLineItems;
import com.example.order.Repository.CartLineItemsRepository;
import com.example.order.Repository.CartRepository;
import com.example.order.exception.ItemNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartLineItemsRepository cartLineItemsRepository;
    private final WebClient.Builder webClientBuilder;
    private CartResponse mapToCartResponse(Cart cart){
        return CartResponse.builder()
                .id(cart.getId())
                .customerId(cart.getCustomerId())
                .cartLineItemsDtoList(cart.getCartLineItemsList().stream()
                        .map(cartLineItems -> CartLineItemsDto.builder()
                                .id(cartLineItems.getId())
                                .price(cartLineItems.getPrice())
                                .skuCode(cartLineItems.getSkuCode())
                                .quantity(cartLineItems.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    private CartLineItems mapToDto(CartLineItemsDto cartLineItemsDto){
        return CartLineItems.builder()
                .skuCode(cartLineItemsDto.getSkuCode())
                .quantity(cartLineItemsDto.getQuantity())
                .build();
    }

    public CartResponse addItemToCart(Long customerId, CartRequest cartRequest) {
        // Retrieve the customer's cart, if it exists, or create a new one
        Cart cart = cartRepository.findByCustomerId(customerId);

        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
        }

        System.out.println("cartRequest: " + cartRequest);

        String skuCode = cartRequest.getCartLineItemsDtoList().getSkuCode();
        int quantity = cartRequest.getCartLineItemsDtoList().getQuantity();

        System.out.println("skuCode: " + skuCode);
        ProductResponse productResponse = webClientBuilder.build().get()
                .uri("http://product/api/product/sku/{skuCode}", skuCode) // Use the desired path
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();

        System.out.println("Product: " + productResponse);
        if(productResponse == null){
            throw new ItemNotFoundException("Product not found for SKU code: " + skuCode);
        }
        if (productResponse.getQuantity() < quantity) {
            throw new ItemNotFoundException("Not enough quantity in stock");
        }

        System.out.println("Product: " + productResponse.getName());

        System.out.println("cart: " + cart);

        // Check if the product is already in the cart, and update the quantity if necessary
            boolean productExistsInCart = false;
        if (cart.getCartLineItemsList() == null) {
            cart.setCartLineItemsList(new ArrayList<>()); // Initialize as an empty list
        }
            for (CartLineItems cartLineItem : cart.getCartLineItemsList()) {
                System.out.println("cartLineItem: " + cartLineItem);
                if (cartLineItem.getSkuCode().equals(skuCode)) {
                    // Product already exists in the cart, update the quantity
                    int newQuantity = cartLineItem.getQuantity() + quantity;
                    if(productResponse.getQuantity() < newQuantity){
                        throw new ItemNotFoundException("Not enough quantity in stock");
                    }
                    cartLineItem.setQuantity(newQuantity);
                    cartLineItem.setPrice(productResponse.getPrice());
                    productExistsInCart = true;
                    break;
                }
            }

            // If the product is not in the cart, create a new CartLineItems and add it to the cart
            if (!productExistsInCart) {
                CartLineItems newLineItem = new CartLineItems();
                newLineItem.setPrice(productResponse.getPrice());
                newLineItem.setSkuCode(skuCode);
                newLineItem.setQuantity(quantity);
                newLineItem.setCart(cart);
                System.out.println("newLineItem: " + newLineItem);

                cartLineItemsRepository.save(newLineItem);

                cart.getCartLineItemsList().add(newLineItem);
            }

                 // Update the product's available quantity
            cartRepository.save(cart);
        return mapToCartResponse(cart);
        }

    public CartResponse getCartForUser(Long userId) {
        // Find the user's active cart
        Cart cart =  cartRepository.findByCustomerId(userId);

        if (cart == null) {
            throw new ItemNotFoundException("Cart not found");
        }
        return mapToCartResponse(cart);
    }

    public CartResponse removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ItemNotFoundException("Cart not found"));

        cart.getCartLineItemsList().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    // In CartService.java
    @Transactional
    public void deleteCartAndLineItems(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Delete associated CartLineItems
        cartLineItemsRepository.deleteByCart(cart);

        // Delete the Cart
        cartRepository.delete(cart);
    }


}
