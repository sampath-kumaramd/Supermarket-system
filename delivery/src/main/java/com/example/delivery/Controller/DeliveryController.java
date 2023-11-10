package com.example.delivery.Controller;

import com.example.delivery.Service.DeliveryService;
import com.example.delivery.dto.DeliveryRequest;
import com.example.delivery.dto.DeliveryResponse;
import com.example.delivery.type.deliveryStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProductCategory(@RequestBody DeliveryRequest deliveryRequest) {
        deliveryService.createDelivery(deliveryRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryResponse getDeliveryById(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateDelivery(
            @PathVariable Long id,
            @RequestBody Map<String , Object> deliveryFields) {
        deliveryService.updateDelivery(id, deliveryFields);
    }

    @PutMapping("/{id}/status")
    public void updateDeliveryStatus(@PathVariable Long id, @RequestBody deliveryStatusType deliveryStatus) {
        deliveryService.updateDeliveryStatus(id, deliveryStatus);
    }

    @PutMapping("/{id}/pickup")
    public void updateDeliveryPerson(@PathVariable Long id, @RequestBody  Long deliveryPersonId) {
        deliveryService.updateDeliveryPerson(id, deliveryPersonId);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.ok("Delivery deleted successfully.");
    }


}
