package com.example.order.Controller;

import com.example.order.Dto.OrderRequest;
import com.example.order.Dto.OrderResponse;
import com.example.order.Service.OrderService;
import com.example.order.type.OrderStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController { private final OrderService orderService;

    @PostMapping("/create")
    public OrderResponse createOrderForCustomer(
            @RequestBody OrderRequest orderRequest) {
        return orderService.createOrderForCustomer(orderRequest);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponse> getAllOrdersForCustomer(@PathVariable Long customerId) {
        return orderService.getAllOrdersForCustomer(customerId);
    }

    @GetMapping("{orderId}")
    public OrderResponse getOrderById(@PathVariable Long orderId ) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/all")
    public List<OrderResponse> getAllOrders() {
        return  orderService.getAllOrders();
    }

    @PutMapping("/update-status/{orderId}")
    public OrderResponse updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusType newStatus) {
        return orderService.updateOrderStatus(orderId, newStatus);
    }


//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
//    @TimeLimiter(name="inventory" )
//    @Retry(name="inventory")
//    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
//     return   CompletableFuture.supplyAsync(()-> orderService.placeOrder(orderRequest));
//    }
//
//    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
//        return CompletableFuture.supplyAsync(()->"Ops! something went wrong, please order after some times");
//    }

}
