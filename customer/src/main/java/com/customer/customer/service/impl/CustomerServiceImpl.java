package com.customer.customer.service.impl;

import com.customer.customer.exception.DatabaseException;
import com.customer.customer.exception.ResourceNotFoundException;
import com.customer.customer.model.Customer;
import com.customer.customer.repository.AddressRepository;
import com.customer.customer.repository.CustomerRepository;
import com.customer.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public Page<Customer> find(Integer page, Integer linesPerPage, String direction, String orderBy) {
        var pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        return customerRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Customer find(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));
    }

    @Transactional
    public Customer create(Customer customer) {
        customer.getAddress().setCustomer(customer);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer update(Long customerId, Customer customerDetails) {
        var customer = find(customerId);

        setCustomerDetails(customer, customerDetails);

        return customerRepository.save(customer);
    }

    private void setCustomerDetails(Customer customer, Customer customerDetails) {
        Optional.ofNullable(customerDetails.getName())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(customer::setName);

        Optional.ofNullable(customerDetails.getAddress())
                .filter(Predicate.not(Objects::isNull))
                .ifPresent(address -> {
                    Optional.ofNullable(address.getStreet())
                            .filter(Predicate.not(String::isBlank))
                            .ifPresent(street -> customer.getAddress().setStreet(street));

                    Optional.ofNullable(address.getNumber())
                            .filter(Predicate.not(String::isBlank))
                            .ifPresent(number -> customer.getAddress().setNumber(number));

                    Optional.ofNullable(address.getNeighborhood())
                            .filter(Predicate.not(String::isBlank))
                            .ifPresent(neighborhood -> customer.getAddress().setNeighborhood(neighborhood));

                    Optional.ofNullable(address.getCity())
                            .filter(Predicate.not(String::isBlank))
                            .ifPresent(city -> customer.getAddress().setCity(city));

                    Optional.ofNullable(address.getState())
                            .filter(Predicate.not(String::isBlank))
                            .ifPresent(state -> customer.getAddress().setState(state));

                    addressRepository.save(address);
                });
    }

    public void delete(Long customerId) {
        var customer = find(customerId);

        try {
            customerRepository.delete(customer);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
