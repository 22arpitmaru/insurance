package com.insurance.service;

import com.insurance.constants.ApplicationConstants;
import com.insurance.exception.CustomException;
import com.insurance.model.Customer;
import com.insurance.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Customer createCustomer(Customer customer) {

		

		logger.info("Creating a new customer with name: {}", customer.getFullName());
		if (customer.getInsuranceTypes().isEmpty())
			throw new CustomException("Customer should have atleast one Insurance product");
		
		List<String> customerInsuranceTypes = customer.getInsuranceTypes().stream().map(String::toUpperCase)
				.collect(Collectors.toList());

		if (!customerInsuranceTypes.stream().anyMatch(ApplicationConstants.INSURANCE_TYPE::contains)) {
			throw new CustomException("Provided Insurenace type is not supported");
		}

		customer.setInsuranceTypes(customerInsuranceTypes);
		Customer savedCustomer = customerRepository.save(customer);
		logger.info("Customer created successfully with ID: {}", savedCustomer.getId());
		return savedCustomer;
	}

	public Optional<Customer> getCustomer(Long id) {
		logger.info("Fetching customer with ID: {}", id);
		Optional<Customer> customer = customerRepository.findById(id);
		if (customer.isPresent()) {
			logger.info("Customer found with ID: {}", id);
		} else {
			logger.warn("Customer with ID {} not found", id);
		}
		return customer;
	}

	public Page<Customer> getAllCustomers(Pageable pageable) {
		logger.info("Fetching all customers with pagination - Page: {}, Size: {}", pageable.getPageNumber(),
				pageable.getPageSize());
		Page<Customer> customers = customerRepository.findAll(pageable);
		logger.info("Found {} customers", customers.getTotalElements());
		return customers;
	}
}
