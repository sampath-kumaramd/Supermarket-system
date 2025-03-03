# Supermarket Management System

A comprehensive microservices-based application for efficient supermarket management built with Spring Boot.

## Project Overview

This project implements a modern microservices architecture to manage various aspects of a supermarket system, including product inventory, order processing, delivery management, and notifications. Each service is designed to operate independently while communicating through a service discovery mechanism.

## Architecture

The system is built using the following microservices:

- **Product Service**: Manages product inventory, categories, and stock levels
- **Order Service**: Handles customer orders and order processing
- **Delivery Service**: Manages delivery logistics and tracking
- **Notification Service**: Handles email notifications for various events
- **API Gateway**: Routes requests to appropriate services
- **Discovery Server**: Service registry for service discovery (Eureka)

## Technologies Used

- **Spring Boot**: Core framework for building microservices
- **Spring Cloud**: For microservices patterns implementation
- **Spring Data JPA**: For database operations
- **MySQL**: Relational database for persistent storage
- **Eureka**: Service discovery
- **Resilience4j**: Circuit breaker implementation
- **Kafka**: Event streaming for asynchronous communication
- **Cloudinary**: Cloud-based image management
- **Spring Mail**: Email notification service

## Key Features

- Product management with categorization
- Order processing with status tracking
- Delivery management and status updates
- Email notifications
- Circuit breaking for fault tolerance
- Service discovery for dynamic service registration
- API Gateway for unified access point
- Image upload and management for products

## Getting Started

### Prerequisites

- Java 17
- Maven
- MySQL
- Kafka

### Running the Application

- Start the MySQL database
- Start Kafka using the provided Docker Compose file:
  ```
  docker-compose up -d
  ```
- Start the Discovery Server first
- Start other services in any order

### Service Ports

- Discovery Server: 8761
- API Gateway: Dynamic (registered with Eureka)
- Order Service: 8081
- Product Service: Dynamic (registered with Eureka)
- Delivery Service: Dynamic (registered with Eureka)
- Notification Service: Dynamic (registered with Eureka)

## API Endpoints

### Product Service

- POST `/api/product/create/{categoryId}`: Create a new product
- GET `/api/product/all`: Get all products
- GET `/api/product/{id}`: Get product by ID
- PUT `/api/product/{id}/update`: Update product
- DELETE `/api/product/{id}/delete`: Delete product
- POST `/api/product/{id}/upload-image`: Upload product image

### Order Service

- POST `/api/order/create`: Create a new order
- GET `/api/order/all`: Get all orders
- GET `/api/order/{orderId}`: Get order by ID
- GET `/api/order/customer/{customerId}`: Get orders by customer ID
- PUT `/api/order/update-status/{orderId}`: Update order status

### Delivery Service

- POST `/api/delivery/create`: Create a new delivery
- GET `/api/delivery/all`: Get all deliveries
- GET `/api/delivery/{id}`: Get delivery by ID
- PUT `/api/delivery/{id}`: Update delivery
- PUT `/api/delivery/{id}/status`: Update delivery status
- PUT `/api/delivery/{id}/pickup`: Assign delivery person

### Notification Service

- POST `/email/send`: Send email notification

## Resilience Patterns

The application implements several resilience patterns:

- Circuit breaker for handling service failures
- Retry mechanism for transient failures
- Timeout handling for unresponsive services

## License

This project is licensed under the MIT License - see the LICENSE file for details.
