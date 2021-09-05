package com.order.order.feignclients;

import com.order.order.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(
        name = "customer-service",
        path = "/api/v1/customers"
)
public interface CustomerFeignClient {

    @GetMapping(value = "/{id}")
    ResponseEntity<Customer> getById(@PathVariable Long id);
}
