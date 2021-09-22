package com.order.order.feignclients;

import com.order.order.model.Delivery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(
        name = "delivery-service",
        path = "/api/v1/deliveries"
)
public interface DeliveryFeignClient {

    @GetMapping("order/{id}")
    ResponseEntity<Delivery> getDeliveryByOrderId(@PathVariable Long id);

    @PostMapping
    ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery);

    @PutMapping("/{id}")
    ResponseEntity<Delivery> updateDelivery(@PathVariable Long id, @RequestBody Delivery deliveryDetails);
}
