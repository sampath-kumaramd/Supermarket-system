package com.example.order.Dto;

import com.example.order.Model.DeliveryAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long customerId;
    private Date orderDate;
    private DeliveryAddress deliveryAddress;
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
