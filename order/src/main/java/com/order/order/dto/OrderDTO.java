package com.order.order.dto;

import com.order.order.model.Customer;
import com.order.order.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private String description;
    private Double value;
    private Customer customer;

    public OrderDTO(Order order) {
        id = order.getId();
        description = order.getDescription();
        value = order.getValue();
    }

    public OrderDTO(Order order, Customer customer) {
        this(order);
        this.customer = customer;
    }
}
