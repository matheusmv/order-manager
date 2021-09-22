package com.order.order.model;

import lombok.Data;

@Data
public class Delivery {

    private Long id;
    private Long orderId;
    private Long customerId;
    private Double shippingValue;
}
