// DeliveryMapper.java
package com.example.delivery.Service;

import com.example.delivery.Entity.Delivery;
import com.example.delivery.dto.DeliveryResponse;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {

    public DeliveryResponse mapToDeliveryResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .delivery_id(delivery.getDelivery_id())
                .order_id(delivery.getOrder_id())
                .delivery_person_id(delivery.getDelivery_person_id())
                .delivery_date(delivery.getDelivery_date())
                .delivery_address_id(delivery.getDelivery_address_id())
                .delivery_status(delivery.getDelivery_status())
                .build();
    }
}
