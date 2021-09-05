package com.delivery.delivery.feignclients;

import com.delivery.delivery.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(
        name = "order-service",
        path = "/api/v1/orders"
)
public interface OrderFeignClient {

    @GetMapping(value = "/{id}")
    ResponseEntity<Order> getById(@PathVariable Long id);
}
