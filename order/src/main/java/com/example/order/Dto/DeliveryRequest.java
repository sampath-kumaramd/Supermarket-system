package com.example.order.Dto;

import com.example.order.Model.DeliveryAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest {
    private Long order_id;
    private Date delivery_date;
    private DeliveryAddress delivery_address;
}
