package com.delivery.delivery.service;

import com.delivery.delivery.dto.DeliveryDTO;
import com.delivery.delivery.exception.DatabaseException;
import com.delivery.delivery.exception.ResourceNotFoundException;
import com.delivery.delivery.feignclients.OrderFeignClient;
import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.repository.DeliveryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderFeignClient orderFeignClient;

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found for this id :: " + deliveryId));
    }

    @Transactional(readOnly = true)
    public DeliveryDTO getDetailedDelivery(Long deliveryId) {
        var delivery = getDeliveryById(deliveryId);
        var order = orderFeignClient.getById(delivery.getOrderId()).getBody();

        return new DeliveryDTO(delivery, order);
    }

    // TODO - when registering a new order create a new delivery
    // TODO - when registering a new delivery check the order id and the customer id
    @Transactional
    public Delivery createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    // TODO FIX
    @Transactional
    public Delivery updateDelivery(Long deliveryId, Delivery deliveryDetails) {
        var delivery = getDeliveryById(deliveryId);

        setDeliveryDetails(delivery, deliveryDetails);

        return deliveryRepository.save(delivery);
    }

    private void setDeliveryDetails(Delivery delivery, Delivery deliveryDetails) {
        Optional.ofNullable(deliveryDetails.getOrderId()).ifPresent(delivery::setOrderId);
        Optional.ofNullable(deliveryDetails.getCustomerId()).ifPresent(delivery::setCustomerId);
        Optional.ofNullable(deliveryDetails.getShippingValue()).ifPresent(delivery::setShippingValue);
    }

    public void deleteDelivery(Long deliveryId) {
        var delivery = getDeliveryById(deliveryId);

        try {
            deliveryRepository.delete(delivery);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
