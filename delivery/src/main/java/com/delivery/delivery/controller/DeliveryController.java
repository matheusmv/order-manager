package com.delivery.delivery.controller;

import com.delivery.delivery.dto.DeliveryDTO;
import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.service.DeliveryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/deliveries")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<Page<Delivery>> getAllDeliveries(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "orderId") String orderBy
    ) {
        var deliveries = deliveryService.find(page, linesPerPage, direction, orderBy);

        return ResponseEntity.ok().body(deliveries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable(value = "id") Long deliveryId) {
        var delivery = deliveryService.findWithOrderAndCustomerDetails(deliveryId);

        return ResponseEntity.ok().body(delivery);
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@Valid @RequestBody Delivery delivery,
                                                   HttpServletRequest request) {
        var newDelivery = deliveryService.create(delivery);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + newDelivery.getId()))
                .body(newDelivery);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable(value = "id") Long deliveryId,
                                                   @Valid @RequestBody Delivery deliveryDetails) {
        var delivery = deliveryService.update(deliveryId, deliveryDetails);

        return ResponseEntity.ok(delivery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable(value = "id") Long deliveryId) {
        deliveryService.delete(deliveryId);

        return ResponseEntity.noContent().build();
    }
}
