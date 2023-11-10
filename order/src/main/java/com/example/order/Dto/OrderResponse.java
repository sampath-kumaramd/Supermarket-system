package com.example.order.Dto;

import com.example.order.Model.DeliveryAddress;
import com.example.order.type.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long customerId;
    private OrderStatusType orderStatus;
    private Date orderDate;

    private DeliveryAddress deliveryAddress;

    private List<OrderLineItemsDto> orderLineItemsList;
}
