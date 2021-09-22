package com.customer.customer.service;

import com.customer.customer.model.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {

    Customer create(Customer customer);

    Customer update(Long id, Customer customer);

    Customer find(Long id);

    Page<Customer> find(Integer page, Integer linesPerPage, String direction, String orderBy);

    void delete(Long id);
}
