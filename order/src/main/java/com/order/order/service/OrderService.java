package com.order.order.service;

import com.order.order.dto.OrderDTO;
import com.order.order.model.Order;
import org.springframework.data.domain.Page;

public interface OrderService {

    Order create(Order order);

    Order update(Long id, Order order);

    Order find(Long id);

    OrderDTO findWithCustomer(Long id);

    Page<Order> find(Integer page, Integer linesPerPage, String direction, String orderBy);

    void delete(Long id);
}
