package com.example.order.Controller;

import com.example.order.Dto.CartLineItemsDto;
import com.example.order.Dto.CartRequest;
import com.example.order.Dto.CartResponse;
import com.example.order.Model.Cart;
import com.example.order.Model.CartLineItems;
import com.example.order.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order/cart")
@RequiredArgsConstructor

public class CartController {

    private final CartService cartService;

    @PostMapping("/{customerId}/add-item")
    public CartResponse addItemToCart(
            @PathVariable Long customerId,
            @RequestBody CartRequest cartRequest
    ) {
        return cartService.addItemToCart(customerId, cartRequest);
    }
    @GetMapping("/{customerId}")
    public CartResponse getActiveCartForUser(@PathVariable Long customerId) {
        return cartService.getCartForUser(customerId);
    }
    @DeleteMapping("/{cartId}/remove-item/{itemId}")
    public CartResponse removeItemFromCart(
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        return cartService.removeItemFromCart(cartId, itemId);
    }

    @DeleteMapping("/clear-cart")
    public ResponseEntity<String> deleteCart() {
        cartService.deleteCartAndLineItems(1L);
        return ResponseEntity.ok("Cart deleted successfully.");
    }

}
