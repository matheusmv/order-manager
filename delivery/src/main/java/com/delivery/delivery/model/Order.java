package com.delivery.delivery.model;

import lombok.Data;

@Data
public class Order {

    private Long id;
    private String description;
    private Double value;
    private Customer customer;
}
