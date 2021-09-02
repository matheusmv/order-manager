package com.order.order.service;

import com.order.order.dto.OrderDTO;
import com.order.order.exception.DatabaseException;
import com.order.order.exception.ResourceNotFoundException;
import com.order.order.feignclients.CustomerFeignClient;
import com.order.order.model.Order;
import com.order.order.repository.OrderRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerFeignClient customerFeignClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + orderId));
    }

    @Transactional(readOnly = true)
    public OrderDTO getDetailedOrder(Long orderId) {
        var order = getOrderById(orderId);
        var customer = customerFeignClient.getById(order.getCustomerId()).getBody();

        return new OrderDTO(order, customer);
    }

    @Transactional
    public Order createOrder(Order order) {
        checkIfTheCustomerIdExists(order.getCustomerId());

        return orderRepository.save(order);
    }

    private void checkIfTheCustomerIdExists(Long customerId) {
        try {
            customerFeignClient.getById(customerId);
        } catch (FeignException exception) {
            throw new ResourceNotFoundException("Customer id does not exist");
        }
    }

    @Transactional
    public Order updateOrder(Long orderId, Order orderDetails) {
        var order = getOrderById(orderId);

        setOrderDetails(order, orderDetails);

        return orderRepository.save(order);
    }

    private void setOrderDetails(Order order, Order orderDetails) {
        Optional.ofNullable(orderDetails.getCustomerId())
                .filter(Predicate.not(Objects::isNull))
                .ifPresent(customerId -> {
                    checkIfTheCustomerIdExists(customerId);
                    order.setCustomerId(customerId);
                });

        Optional.ofNullable(orderDetails.getDescription())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(order::setDescription);

        Optional.ofNullable(orderDetails.getValue())
                .filter(value -> value > 0)
                .ifPresent(order::setValue);
    }

    public void deleteOrder(Long orderId) {
        var order = getOrderById(orderId);

        try {
            orderRepository.delete(order);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
