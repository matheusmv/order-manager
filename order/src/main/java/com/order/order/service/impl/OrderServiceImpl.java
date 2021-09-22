package com.order.order.service.impl;

import com.order.order.dto.OrderDTO;
import com.order.order.exception.DatabaseException;
import com.order.order.exception.ResourceNotFoundException;
import com.order.order.feignclients.CustomerFeignClient;
import com.order.order.model.Order;
import com.order.order.repository.OrderRepository;
import com.order.order.service.OrderService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerFeignClient customerFeignClient;

    public Page<Order> find(Integer page, Integer linesPerPage, String direction, String orderBy) {
        var pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        return orderRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Order find(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + orderId));
    }

    @Transactional(readOnly = true)
    public OrderDTO findWithCustomer(Long orderId) {
        var order = find(orderId);
        var customer = customerFeignClient.getById(order.getCustomerId()).getBody();

        return new OrderDTO(order, customer);
    }

    @Transactional
    public Order create(Order order) {
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
    public Order update(Long orderId, Order orderDetails) {
        var order = find(orderId);

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

    public void delete(Long orderId) {
        var order = find(orderId);

        try {
            orderRepository.delete(order);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
