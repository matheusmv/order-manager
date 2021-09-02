package com.delivery.delivery.controller;

import com.delivery.delivery.dto.DeliveryDTO;
import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.service.DeliveryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        var deliveries = deliveryService.getAllDeliveries();

        return ResponseEntity.ok().body(deliveries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable(value = "id") Long deliveryId) {
        var delivery = deliveryService.getDetailedDelivery(deliveryId);

        return ResponseEntity.ok().body(delivery);
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@Valid @RequestBody Delivery delivery,
                                                   HttpServletRequest request) {
        var newDelivery = deliveryService.createDelivery(delivery);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + newDelivery.getId()))
                .body(newDelivery);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable(value = "id") Long deliveryId,
                                                   @Valid @RequestBody Delivery deliveryDetails) {
        var delivery = deliveryService.updateDelivery(deliveryId, deliveryDetails);

        return ResponseEntity.ok(delivery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable(value = "id") Long deliveryId) {
        deliveryService.deleteDelivery(deliveryId);

        return ResponseEntity.noContent().build();
    }
}
