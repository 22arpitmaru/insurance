package com.insurance.controller;

import com.insurance.model.Customer;
import com.insurance.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setFullName("John Doe");
    }

    @Test
    void createCustomer_Success() {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.createCustomer(customer);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("John Doe", response.getBody().getFullName());
        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    void getCustomer_Success() {
        when(customerService.getCustomer(1L)).thenReturn(Optional.of(customer));

        ResponseEntity<Customer> response = customerController.getCustomer(1L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("John Doe", response.getBody().getFullName());
        verify(customerService, times(1)).getCustomer(1L);
    }

    @Test
    void getCustomer_NotFound() {
        when(customerService.getCustomer(1L)).thenReturn(Optional.empty());

        ResponseEntity<Customer> response = customerController.getCustomer(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(customerService, times(1)).getCustomer(1L);
    }

    @Test
    void getAllCustomers_Success() {
        List<Customer> customerList = Arrays.asList(customer, new Customer());
        Page<Customer> customerPage = new PageImpl<>(customerList);
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(customerService.getAllCustomers(pageRequest)).thenReturn(customerPage);

        ResponseEntity<Page<Customer>> response = customerController.getAllCustomers(0, 10);

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        verify(customerService, times(1)).getAllCustomers(pageRequest);
    }
}
