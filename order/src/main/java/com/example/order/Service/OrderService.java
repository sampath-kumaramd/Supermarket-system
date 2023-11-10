package com.example.order.Service;

import com.example.order.Dto.*;
import com.example.order.Model.DeliveryAddress;
import com.example.order.Model.Order;
import com.example.order.Model.OrderLineItems;
import com.example.order.Repository.DeliveryAddressRepository;
import com.example.order.Repository.OrderLineItemsRepository;
import com.example.order.Repository.OrderRepository;
import com.example.order.event.OrderPlacedEvent;
import com.example.order.type.OrderStatusType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemsRepository orderLineItemsRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    private OrderResponse mapToOrderResponse(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .orderDate(order.getOrderDate())
                .deliveryAddress(order.getDeliveryAddress())
                .orderLineItemsList(order.getOrderLineItemsList().stream()
                        .map(orderLineItems -> OrderLineItemsDto.builder()
                                .price(orderLineItems.getPrice())
                                .skuCode(orderLineItems.getSkuCode())
                                .quantity(orderLineItems.getQuantity())
                                .build())
                        .toList())
                .orderStatus(order.getOrderStatus())
                .build();
    }
    @Transactional
    public OrderResponse createOrderForCustomer(OrderRequest orderRequest) {
        // Create a new order
        Order order = new Order();
        order.setCustomerId(orderRequest.getCustomerId());
        order.setOrderStatus(OrderStatusType.PENDING);
        order.setDeliveryAddress(orderRequest.getDeliveryAddress());
        order.setOrderDate(orderRequest.getOrderDate());

        // Save the delivery address separately
        DeliveryAddress deliveryAddress = deliveryAddressRepository.save(orderRequest.getDeliveryAddress());
        order.setDeliveryAddress(deliveryAddress);

        List<OrderLineItemsDto> lineItemsDtoList = orderRequest.getOrderLineItemsDtoList();
        List<OrderLineItems> orderLineItemsList = new ArrayList<>();
        List<String> skuCodes = new ArrayList<>();
        List<Integer> quantitiesToAdd = new ArrayList<>();

        // Create order line items from the DTOs
        for (OrderLineItemsDto dto : lineItemsDtoList) {
            OrderLineItems orderLineItem = new OrderLineItems();
            orderLineItem.setSkuCode(dto.getSkuCode());
            orderLineItem.setQuantity(dto.getQuantity());
            orderLineItem.setPrice(dto.getPrice());
            orderLineItem.setOrder(order);
            orderLineItemsList.add(orderLineItem);
            skuCodes.add(dto.getSkuCode());
            quantitiesToAdd.add(dto.getQuantity());
        }

        System.out.println("skuCodes: " + skuCodes);
        System.out.println("quantitiesToAdd: " + quantitiesToAdd);


        QuantityRequest request = new QuantityRequest();
        request.setSkuCodes(skuCodes);
        request.setQuantities(quantitiesToAdd);


        String ProductServiceUrl = "http://product/api/product/reduce-quantities";

        try {
            webClientBuilder.build()
                    .put()
                    .uri(ProductServiceUrl)
                    .bodyValue(request)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() != HttpStatus.CREATED) {
                throw new RuntimeException("Failed to create delivery. HTTP status: " + e.getStatusCode(), e);
            }
        }

        order.setOrderLineItemsList(orderLineItemsList);

        // Save the order and associated line items
        order = orderRepository.save(order);
        orderLineItemsRepository.saveAll(orderLineItemsList);
        return mapToOrderResponse(order);
    }

    public List<OrderResponse> getAllOrdersForCustomer(Long customerId) {
       List<Order> order = orderRepository.findByCustomerId(customerId);
         return order.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders= orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

       public OrderResponse getOrderById(Long orderId) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
            return mapToOrderResponse(order);
        }
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusType newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        if((order.getOrderStatus().equals(OrderStatusType.PROCESSING ) || (order.getOrderStatus().equals(OrderStatusType.READY_TO_SHIP  ))) && newStatus.equals(OrderStatusType.CANCELED)){
            throw new IllegalArgumentException("Order status is already preparing, cannot be canceled");
        }

//        if(order.getOrderStatus().equals(OrderStatusType.PENDING) && newStatus.equals(OrderStatusType.PROCESSING)){
//            order.setOrderStatus(newStatus);
//            order = orderRepository.save(order);
//            kafkaTemplate.send("notificationTopic" , new OrderPlacedEvent(order.getId().toString()));
//            return mapToOrderResponse(order);
//        }


        if (order.getOrderStatus().equals(OrderStatusType.PENDING) && newStatus.equals(OrderStatusType.READY_TO_SHIP)) {
            // Assuming DeliveryRequest is correctly constructed based on your requirements
            DeliveryRequest deliveryRequest = new DeliveryRequest();
            deliveryRequest.setOrder_id(orderId);
            deliveryRequest.setDelivery_date(order.getOrderDate());
            deliveryRequest.setDelivery_address(order.getDeliveryAddress());
            // Set any other required fields in the delivery request

            // Make an HTTP POST request to create a delivery
            String deliveryServiceUrl = "http://delivery/api/delivery/create";
            try {
                webClientBuilder.build()
                        .post()
                        .uri(deliveryServiceUrl)
                        .bodyValue(deliveryRequest)
                        .retrieve()
                        .toBodilessEntity()
                        .block();
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.CREATED) {
                    throw new RuntimeException("Failed to create delivery. HTTP status: " + e.getStatusCode(), e);
                }
            }

            // Continue with updating the order status
            order.setOrderStatus(newStatus);
            order = orderRepository.save(order);
            return mapToOrderResponse(order);
        }

        if(order.getOrderStatus().equals(OrderStatusType.PENDING) && newStatus.equals(OrderStatusType.CANCELED)){

            List<String> skuCodes = order.getOrderLineItemsList().stream()
                    .map(OrderLineItems::getSkuCode)
                    .toList();

            List<Integer> quantitiesToAddList = order.getOrderLineItemsList().stream()
                    .map(OrderLineItems::getQuantity)
                    .toList();

            String skuCodesStr = String.join(",", skuCodes);
            String quantitiesToAdd = quantitiesToAddList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            QuantityRequest request = new QuantityRequest();

            request.setSkuCodes(skuCodes);
            request.setQuantities(quantitiesToAddList);

            String ProductServiceUrl = "http://product/api/product/add-quantities";

            try {
                webClientBuilder.build()
                        .put()
                        .uri(ProductServiceUrl)
                        .bodyValue(request)
                        .retrieve()
                        .toBodilessEntity()
                        .block();
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.CREATED) {
                    throw new RuntimeException("Failed to create delivery. HTTP status: " + e.getStatusCode(), e);
                }
            }

        }
        order.setOrderStatus(newStatus);
        order = orderRepository.save(order);
        kafkaTemplate.send("notificationTopic" , new OrderPlacedEvent(order.getId().toString()));
        return mapToOrderResponse(order);
    }













//    public String placeOrder(OrderRequest orderRequest) {
//        Order order = new Order();
//
//        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
//
//        order.setOrderLineItemsList(orderLineItems);
//        List<String> skuCodes = order.getOrderLineItemsList().stream()
//                .map(OrderLineItems::getSkuCode)
//                .toList();
//        ProductResponse[] inventoryResponseArray = webClientBuilder.build().get()
//                .uri("http://inventory/api/inventory",
//                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
//                .retrieve()
//                .bodyToMono(ProductResponse[].class)
//                .block();
//
//        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
//                .allMatch(ProductResponse::isInStock);
//
//        if (allProductsInStock) {
//            orderRepository.save(order);
//            kafkaTemplate.send("notificationTopic" , new OrderPlacedEvent(order.getId().toString()));
//            return "order placed successfully";
//            // publish Order Placed Event
//
//
//        } else {
//            throw new IllegalArgumentException("Product is not in stock, please try again later");
//        }
//    }
//




    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }}
