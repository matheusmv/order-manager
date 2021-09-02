package com.delivery.delivery.model;

import lombok.Data;

@Data
public class Customer {

    private Long id;
    private String name;
    private Address address;
}
