package com.delivery.delivery.service;

import com.delivery.delivery.dto.DeliveryDTO;
import com.delivery.delivery.model.Delivery;
import org.springframework.data.domain.Page;

public interface DeliveryService {

    Delivery create(Delivery customer);

    Delivery update(Long id, Delivery customer);

    Delivery find(Long id);

    Delivery findByOrderId(Long id);

    Delivery findByCustomerId(Long id);

    DeliveryDTO findWithOrderAndCustomerDetails(Long id);

    Page<Delivery> find(Integer page, Integer linesPerPage, String direction, String orderBy);

    void delete(Long id);
}
