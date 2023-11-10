// DeliveryUpdater.java
package com.example.delivery.Service;

import com.example.delivery.Entity.Delivery;
import com.example.delivery.Entity.DeliveryAddress;
import com.example.delivery.Entity.DeliveryStatus;
import com.example.delivery.Repository.DeliveryRepository;
import com.example.delivery.Repository.DeliveryStatusRepository;
import com.example.delivery.type.deliveryStatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class DeliveryUpdater {

    void updateDeliveryFields(Delivery delivery, Map<String, Object> deliveryFields) {
        for (Map.Entry<String, Object> entry : deliveryFields.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equals("order_id")) {
                delivery.setOrder_id(((Number) value).longValue());
            } else if (key.equals("delivery_person_id")) {
                delivery.setDelivery_person_id(((Number) value).longValue());
            } else if (key.equals("delivery_date")) {
                updateDeliveryDate(delivery, (String) value);
            } else if (key.equals("delivery_address_id")) {
                if (value instanceof Map) {
                    // Convert the Map to DeliveryAddress
                    ObjectMapper objectMapper = new ObjectMapper();
                    DeliveryAddress deliveryAddress = objectMapper.convertValue(value, DeliveryAddress.class);
                    updateDeliveryAddress(delivery, deliveryAddress);
                } else {
                    throw new IllegalArgumentException("Invalid format for 'delivery_address_id'");
                }
            }
            // Add more fields to update as needed
        }
    }


    Delivery  updateDeliveryPerson(Delivery delivery, Long deliveryPersonId) {
        updateDeliveryStatus(delivery, deliveryStatusType.PICKED_UP);
        delivery.setDelivery_person_id(deliveryPersonId);
        return delivery;
    }

    // Make this method package-private or protected
    Delivery updateDeliveryStatus(Delivery delivery, deliveryStatusType deliveryStatus) {
        DeliveryStatus newDeliveryStatus = new DeliveryStatus();
        newDeliveryStatus.setDelivery_status(deliveryStatus);
        newDeliveryStatus.setDelivery_status_date(new Date());
        delivery.getDelivery_status().add(newDeliveryStatus);
        return delivery;
    }

    private void updateDeliveryDate(Delivery delivery, String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateString);
            delivery.setDelivery_date(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateDeliveryAddress(Delivery delivery, DeliveryAddress deliveryAddress) {
        delivery.setDelivery_address_id(deliveryAddress);
    }
}
