package com.order.order.model;

import lombok.Data;

@Data
public class Address {

    private Long id;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
}
