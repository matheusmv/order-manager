package com.delivery.delivery.service;

import com.delivery.delivery.exception.ResourceNotFoundException;
import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.repository.DeliveryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found for this id :: " + deliveryId));
    }

    public Delivery createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public Delivery updateDelivery(Long deliveryId, Delivery deliveryDetails) {
        var delivery = getDeliveryById(deliveryId);

        setDeliveryDetails(delivery, deliveryDetails);

        return deliveryRepository.save(delivery);
    }

    private void setDeliveryDetails(Delivery delivery, Delivery deliveryDetails) {
        Optional.ofNullable(deliveryDetails.getOrderId()).ifPresent(delivery::setOrderId);
        Optional.ofNullable(deliveryDetails.getCustomerId()).ifPresent(delivery::setCustomerId);
        Optional.ofNullable(deliveryDetails.getAddress()).ifPresent(delivery::setAddress);
        Optional.ofNullable(deliveryDetails.getShippingValue()).ifPresent(delivery::setShippingValue);
    }

    public void deleteDelivery(Long deliveryId) {
        var delivery = getDeliveryById(deliveryId);

        deliveryRepository.delete(delivery);
    }
}
