package com.insurance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.insurance.model.Customer;
import com.insurance.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        logger.info("Received request to create a new customer: {}", customer.getFullName());
        Customer createdCustomer = customerService.createCustomer(customer);
        logger.info("Customer created successfully with ID: {}", createdCustomer.getId());
        return ResponseEntity.ok(createdCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        logger.info("Received request to fetch customer with ID: {}", id);
        return customerService.getCustomer(id)
                .map(customer -> {
                    logger.info("Customer found with ID: {}", id);
                    return ResponseEntity.ok(customer);
                })
                .orElseGet(() -> {
                    logger.warn("Customer with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    public ResponseEntity<Page<Customer>> getAllCustomers(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        logger.info("Received request to fetch all customers with pagination - Page: {}, Size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        logger.info("Found {} customers on page {}", customers.getTotalElements(), page);
        return ResponseEntity.ok(customers);
    }
}
