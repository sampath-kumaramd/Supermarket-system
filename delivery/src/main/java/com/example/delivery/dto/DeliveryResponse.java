package com.example.delivery.dto;

import com.example.delivery.Entity.DeliveryAddress;
import com.example.delivery.Entity.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponse {
    private Long delivery_id;
    private Long order_id;
    private Long delivery_person_id;
    private Date delivery_date;
    private DeliveryAddress delivery_address_id;
    private List<DeliveryStatus> delivery_status;
}
