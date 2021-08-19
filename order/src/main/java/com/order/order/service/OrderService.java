package com.order.order.service;

import com.order.order.exception.ResourceNotFoundException;
import com.order.order.model.Order;
import com.order.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + orderId));
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, Order orderDetails) {
        var order = getOrderById(orderId);

        setOrderDetails(order, orderDetails);

        return orderRepository.save(order);
    }

    private void setOrderDetails(Order order, Order orderDetails) {
        Optional.ofNullable(orderDetails.getCustomerId()).ifPresent(order::setCustomerId);
        Optional.ofNullable(orderDetails.getDescription()).ifPresent(order::setDescription);
        Optional.ofNullable(orderDetails.getValue()).ifPresent(order::setValue);
    }

    public void deleteOrder(Long orderId) {
        var order = getOrderById(orderId);

        orderRepository.delete(order);
    }
}
