package com.customer.customer.controller;

import com.customer.customer.model.Customer;
import com.customer.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        var customers = customerService.getAllCustomers();

        return ResponseEntity.ok().body(customers);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id") Long customerId) {
        var customer = customerService.getCustomerById(customerId);

        return ResponseEntity.ok().body(customer);
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer,
                                                   HttpServletRequest request) {
        var newCustomer = customerService.createCustomer(customer);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + newCustomer.getId()))
                .body(newCustomer);
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "id") Long customerId,
                                                   @Valid @RequestBody Customer customerDetails) {
        var customer = customerService.updateCustomer(customerId, customerDetails);

        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable(value = "id") Long customerId) {
        customerService.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }
}
