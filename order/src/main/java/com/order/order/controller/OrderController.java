package com.order.order.controller;

import com.order.order.model.Order;
import com.order.order.service.OrderService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/v1")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        var orders = orderService.getAllOrders();

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable(value = "id") Long orderId) {
        var order = orderService.getOrderById(orderId);

        return ResponseEntity.ok().body(order);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order,
                                             HttpServletRequest request) {
        var newOrder = orderService.createOrder(order);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + newOrder.getId()))
                .body(newOrder);
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable(value = "id") Long orderId,
                                             @Valid @RequestBody Order orderDetails) {
        var order = orderService.updateOrder(orderId, orderDetails);

        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable(value = "id") Long orderId) {
        orderService.deleteOrder(orderId);

        return ResponseEntity.noContent().build();
    }
}
