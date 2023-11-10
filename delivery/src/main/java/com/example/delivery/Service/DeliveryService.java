// DeliveryService.java
package com.example.delivery.Service;

import com.example.delivery.Entity.Delivery;
import com.example.delivery.Entity.DeliveryStatus;
import com.example.delivery.Repository.DeliveryRepository;
import com.example.delivery.Repository.DeliveryStatusRepository;
import com.example.delivery.dto.DeliveryRequest;
import com.example.delivery.dto.DeliveryResponse;
import com.example.delivery.type.deliveryStatusType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryStatusRepository deliveryStatusRepository;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryValidator deliveryValidator;
    private final DeliveryUpdater deliveryUpdater;

    public void createDelivery(DeliveryRequest deliveryRequest) {
        Delivery delivery = new Delivery();
        DeliveryStatus newDeliveryStatus = new DeliveryStatus();
        newDeliveryStatus.setDelivery_status(deliveryStatusType.READY_TO_DELIVER);
        newDeliveryStatus.setDelivery_status_date(new Date());
        delivery.setOrder_id(deliveryRequest.getOrder_id());
        delivery.setDelivery_date(deliveryRequest.getDelivery_date());
        delivery.setDelivery_address_id(deliveryRequest.getDelivery_address());
        delivery.setDelivery_status(List.of(newDeliveryStatus));
        deliveryRepository.save(delivery);
    }

    public List<DeliveryResponse> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream().map(deliveryMapper::mapToDeliveryResponse).toList();
    }

    public DeliveryResponse getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        return deliveryMapper.mapToDeliveryResponse(delivery);
    }

    @Transactional
    public void updateDelivery(Long id, Map<String, Object> deliveryFields) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        deliveryValidator.validateDeliveryStatusForUpdate(delivery);
        deliveryUpdater.updateDeliveryFields(delivery, deliveryFields);

        deliveryRepository.save(delivery);
    }

    public void updateDeliveryStatus(Long deliveryId,  deliveryStatusType deliveryStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        deliveryValidator.validateDeliveryStatusNotExists(delivery, deliveryStatus);
       Delivery _delivery = deliveryUpdater.updateDeliveryStatus(delivery, deliveryStatus);
       deliveryRepository.save(_delivery);
    }

    public void updateDeliveryPerson(Long deliveryId, Long deliveryPersonId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        deliveryValidator.validateDeliveryStatusForUpdate(delivery);
        Delivery _delivery = deliveryUpdater.updateDeliveryPerson(delivery, deliveryPersonId);
        deliveryRepository.save(_delivery);
    }

    @Transactional
    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        deliveryValidator.validateDeliveryStatusForUpdate(delivery);
        deliveryRepository.delete(delivery);
    }
}
