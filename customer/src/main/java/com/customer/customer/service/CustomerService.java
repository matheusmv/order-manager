package com.customer.customer.service;

import com.customer.customer.exception.ResourceNotFoundException;
import com.customer.customer.model.Customer;
import com.customer.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long customerId, Customer customerDetails) {
        var customer = getCustomerById(customerId);

        setCustomerDetails(customer, customerDetails);

        return customerRepository.save(customer);
    }

    private void setCustomerDetails(Customer customer, Customer customerDetails) {
        Optional.ofNullable(customerDetails.getName()).ifPresent(customer::setName);
        Optional.ofNullable(customerDetails.getAddress()).ifPresent(customer::setAddress);
    }

    public void deleteCustomer(Long customerId) {
        var customer = getCustomerById(customerId);

        customerRepository.delete(customer);
    }
}
