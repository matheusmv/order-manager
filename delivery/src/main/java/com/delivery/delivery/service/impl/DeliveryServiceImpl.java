package com.delivery.delivery.service.impl;

import com.delivery.delivery.dto.DeliveryDTO;
import com.delivery.delivery.exception.DatabaseException;
import com.delivery.delivery.exception.ResourceNotFoundException;
import com.delivery.delivery.feignclients.OrderFeignClient;
import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.repository.DeliveryRepository;
import com.delivery.delivery.service.DeliveryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderFeignClient orderFeignClient;

    public Page<Delivery> find(Integer page, Integer linesPerPage, String direction, String orderBy) {
        var pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        return deliveryRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Delivery find(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found for this id :: " + deliveryId));
    }

    @Transactional(readOnly = true)
    public Delivery findByOrderId(Long id) {
        return deliveryRepository.findByOrderId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found for this order id :: " + id));
    }

    @Transactional(readOnly = true)
    public Delivery findByCustomerId(Long id) {
        return deliveryRepository.findByCustomerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found for this customer id :: " + id));
    }

    @Transactional(readOnly = true)
    public DeliveryDTO findWithOrderAndCustomerDetails(Long deliveryId) {
        var delivery = find(deliveryId);
        var order = orderFeignClient.getById(delivery.getOrderId()).getBody();

        return new DeliveryDTO(delivery, order);
    }

    @Transactional
    public Delivery create(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Transactional
    public Delivery update(Long deliveryId, Delivery deliveryDetails) {
        var delivery = find(deliveryId);

        setDeliveryDetails(delivery, deliveryDetails);

        return deliveryRepository.save(delivery);
    }

    private void setDeliveryDetails(Delivery delivery, Delivery deliveryDetails) {
        Optional.ofNullable(deliveryDetails.getOrderId()).ifPresent(delivery::setOrderId);
        Optional.ofNullable(deliveryDetails.getCustomerId()).ifPresent(delivery::setCustomerId);
        Optional.ofNullable(deliveryDetails.getShippingValue()).ifPresent(delivery::setShippingValue);
    }

    public void delete(Long deliveryId) {
        var delivery = find(deliveryId);

        try {
            deliveryRepository.delete(delivery);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
