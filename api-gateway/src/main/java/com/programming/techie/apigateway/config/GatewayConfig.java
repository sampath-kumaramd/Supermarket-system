package com.programming.techie.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Example code using Spring Cloud Gateway
@Configuration
public class GatewayConfig {

    @Autowired
    private AuthGatewayFilterFactory authGatewayFilterFactory;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("lb://auth-service/**"))

             //UserMicroService
                // customer
                .route("UserManagementService", r -> r
                        .path("/customers/add")
                        .uri("lb://UserManagementService/**"))
                .route("UserManagementService", r -> r
                        .path("/customers/**")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"ADMIN","CUSTOMER"}))))
                        .uri("lb://UserManagementService/**"))

                .route("UserManagementService", r -> r
                        .path("/admin/**")
                        .uri("lb://UserManagementService/**"))

              //delivery
                .route("UserManagementService", r -> r
                        .path("/deliveryPerson/**")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"ADMIN"}))))
                        .uri("lb://UserManagementService/**"))

                //inventory
                .route("UserManagementService", r -> r
                        .path("/inventoryManager/**")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"ADMIN"}))))
                        .uri("lb://UserManagementService/**"))


                //ProductMicroservice
                //product
                .route("product", r -> r
                        .path("/api/product//create/{categoryId}")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
                        .uri("lb://product/**"))
                .route("product", r -> r
                        .path("/api/product/{id}/upload-image")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
                        .uri("lb://product/**"))
                .route("product", r -> r
                        .path("/api/product/{id}/update")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
                        .uri("lb://product/**"))
                .route("product", r -> r
                        .path("/api/product/{id}/delete")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
                        .uri("lb://product/**"))

                .route("product", r -> r
                        .path("/api/product/**")
                        .uri("lb://product/**"))

                //productcatogary
                .route("product", r -> r
                        .path("/api/product/product-category/create")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
                        .uri("lb://product/**"))
                .route("product", r -> r
                        .path("/api/product/product-category/{categoryId}/delete")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
                        .uri("lb://product/**"))
                .route("product", r -> r
                        .path("/api/product/product-category/{id}")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
                        .uri("lb://product/**"))
                .route("product", r -> r
                        .path("/api/product/product-category/**")
                        .uri("lb://product/**"))
              //OrderMicroservice
                //order
                .route("order", r -> r
                        .path("/api/order/create")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"CUSTOMER"}))))
                        .uri("lb://order/**"))
                //cart
                .route("order", r -> r
                        .path("/api/order/cart/**")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"CUSTOMER"}))))
                        .uri("lb://order/**"))

                .route("order", r -> r
                        .path("/api/order/**")
                        .uri("lb://order/**"))


                //Delivery
//                .route("order", r -> r
//                        .path("/api/order/cart/**")
//                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"CUSTOMER"}))))
//                        .uri("lb://order/**"))
                .route("delivery", r -> r
                        .path("/api/delivery/**")
                        .uri("lb://delivery/**"))









//                .route("UserManagementService", r -> r
//                        .path("/inventoryManager/getById/{inventoryManagerId}")
//                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
//                        .uri("lb://UserManagementService/**"))
//                .route("UserManagementService", r -> r
//                        .path("/inventoryManager/**")
//                        .uri("lb://UserManagementService/**"))

//                .route("auth-service", r -> r
//                        .path("/auth/authenticate")
//                        .uri("lb://auth-service/**"))

//                .route("customer-service", r -> r
//                        .path("/customers/**")
//                        .uri("lb://customer-service/**"))
//                .route("customer-service", r -> r
//                        .path("/customers/add")
//                        .uri("lb://customer-service/**"))
//                .route("inventorymanager-service", r -> r
//                        .path("/inventoryManager/getById/{inventoryManagerId}")
//                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
//                        .uri("lb://inventorymanager-service/**"))
//                .route("inventorymanager-service", r -> r
//                        .path("/inventoryManager/**")
//                        .uri("lb://inventorymanager-service/**"))

//                .route("deliveryperson-service", r -> r
//                        .path("/deliveryPerson/**")
//                        .uri("lb://deliveryperson-service/**"))
//                .route("delivery-person", r -> r
//                        .path("/deliveryPerson/getDeliveryPersonById/{deliveryPersonId}")
//                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"DELIVERY_PERSON","ADMIN"}))))
//                        .uri("lb://deliveryperson-service/**"))

//                .route("customer-service", r -> r
//                        .path("/customers/add")
//                        .uri("lb://customer-service/**"))
//                .route("customer-service", r -> r
//                        .path("/customers/getAll")
//                        .uri("lb://customer-service/**"))
//                .route("auth-service", r -> r
//                        .path("/auth/register")
//                        .uri("lb://auth-service/**"))
//                .route("customer-service", r -> r
//                        .path("/customers/getAll")
//                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"CUSTOMER", "ADMIN"}))))
//                        .uri("lb://customer-service/**"))

            //getoriginal



//                .route("product-service", r -> r
//                        .path("/api/product/**")
//                        .uri("lb://product-service/**"))
//
//                .route("delivery-service", r -> r
//                        .path("/api/delivery/**")
//                        .uri("lb://delivery-service/**"))
//
//                .route("order-service", r -> r
//                        .path("/api/order/**")
//                        .uri("lb://order-service/**"))

                .route("discovery-server", r -> r
                        .path("/eureka/web/**")
                        .uri("http://localhost:8761"))

                .route("discovery-server-static", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))






















//                .route("product-service", r -> r
//                        .path("/products/**")
//                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"INVENTORY_KEEPER"}))))
//                        .uri("http://localhost:8070"))
//                .route("delivery-service", r -> r
//                        .path("/products/**")
//                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"DELIVERY_PERSON"}))))
//                        .uri("http://localhost:8050"))
//                .route("order-service", r -> r
//                        .path("/orders/**")
//                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config(new String[]{"DELIVERY_PERSON"}))))
//                        .uri("http://localhost:8060"))
                .build();
    }
}


