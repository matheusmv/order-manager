package com.order.order.controller;

import com.order.order.dto.OrderDTO;
import com.order.order.model.Order;
import com.order.order.service.OrderService;
import lombok.AllArgsConstructor;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "value") String orderBy
    ) {
        var orders = orderService.find(page, linesPerPage, direction, orderBy);

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable(value = "id") Long orderId) {
        var order = orderService.findWithCustomer(orderId);

        return ResponseEntity.ok().body(order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {

        var newOrder = orderService.create(order);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newOrder.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable(value = "id") Long orderId,
                                             @RequestBody Order orderDetails) {
        var order = orderService.update(orderId, orderDetails);

        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable(value = "id") Long orderId) {
        orderService.delete(orderId);

        return ResponseEntity.noContent().build();
    }
}
