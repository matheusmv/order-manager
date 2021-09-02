package com.customer.customer.service;

import com.customer.customer.exception.DatabaseException;
import com.customer.customer.exception.ResourceNotFoundException;
import com.customer.customer.model.Customer;
import com.customer.customer.repository.AddressRepository;
import com.customer.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        customer.getAddress().setCustomer(customer);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long customerId, Customer customerDetails) {
        var customer = getCustomerById(customerId);

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

    public void deleteCustomer(Long customerId) {
        var customer = getCustomerById(customerId);

        try {
            customerRepository.delete(customer);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
