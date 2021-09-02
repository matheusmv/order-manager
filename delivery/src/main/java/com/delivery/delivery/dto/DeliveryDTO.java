package com.delivery.delivery.dto;

import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryDTO {

    private Long id;
    private Double shippingValue;
    private Order order;

    public DeliveryDTO(Delivery delivery) {
        id = delivery.getId();
        shippingValue = delivery.getShippingValue();
    }

    public DeliveryDTO(Delivery delivery, Order order) {
        this(delivery);
        this.order = order;
    }
}
