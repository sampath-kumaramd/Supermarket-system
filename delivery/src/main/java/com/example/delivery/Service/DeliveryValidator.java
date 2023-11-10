// DeliveryValidator.java
package com.example.delivery.Service;

import com.example.delivery.Entity.Delivery;
import com.example.delivery.Entity.DeliveryStatus;
import com.example.delivery.type.deliveryStatusType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryValidator {

    void validateDeliveryStatusForUpdate(Delivery delivery) {
        for (DeliveryStatus deliveryStatus : delivery.getDelivery_status()) {
            if (!(deliveryStatus.getDelivery_status().equals(deliveryStatusType.READY_TO_DELIVER))) {
                throw new RuntimeException("Delivery status is not pending");
            }
        }
    }

    void validateDeliveryStatusNotExists(Delivery delivery, deliveryStatusType statusToCheck) {
        List<DeliveryStatus> deliveryStatusList = delivery.getDelivery_status();
        for (DeliveryStatus existingStatus : deliveryStatusList) {
            if (existingStatus.getDelivery_status().equals(statusToCheck)) {
                throw new RuntimeException("Delivery status already exists: " + statusToCheck);
            }
        }
    }

}
