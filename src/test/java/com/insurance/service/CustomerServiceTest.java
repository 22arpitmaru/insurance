package com.insurance.service;

import com.insurance.model.Customer;
import com.insurance.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a mock customer with insurance types
        customer = new Customer();
        customer.setId(1L);
        customer.setFullName("John Doe");
        customer.setDateOfBirth(new Date());
        customer.setInsuranceTypes(Arrays.asList("Car", "Health"));
    }

    @Test
    void createCustomer_Success() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals(2, result.getInsuranceTypes().size());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void getCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomer(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getFullName());
        assertEquals(2, result.get().getInsuranceTypes().size());
        assertTrue(result.get().getInsuranceTypes().contains("Car"));
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getCustomer(1L);

        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getAllCustomers_Success() {
        List<Customer> customers = Arrays.asList(customer, new Customer());
        Page<Customer> customerPage = new PageImpl<>(customers);
        Pageable pageable = PageRequest.of(0, 10);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> result = customerService.getAllCustomers(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(customerRepository, times(1)).findAll(pageable);
    }
}
