package com.insurance.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.dto.request.InsuranceClaimDto;
import com.insurance.exception.ClaimNotProcessedException;
import com.insurance.exception.CustomerNotFoundException;
import com.insurance.model.Customer;
import com.insurance.model.InsuranceClaim;
import com.insurance.model.enums.ClaimStatus;
import com.insurance.model.enums.ClaimType;
import com.insurance.repository.InsuranceClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsuranceClaimServiceTest {

    @InjectMocks
    private InsuranceClaimService insuranceClaimService;

    @Mock
    private InsuranceClaimRepository insuranceClaimRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ObjectMapper objectMapper;

    private InsuranceClaimDto claimDto;
    private Customer customer;
    private InsuranceClaim insuranceClaim;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        claimDto = new InsuranceClaimDto();
        claimDto.setCustomerId(1L);
        claimDto.setClaimType("CAR");
        claimDto.setClaimDate(new java.util.Date());
        claimDto.setStatus(ClaimStatus.OPEN);
        claimDto.setCost(5000.00);

        customer = new Customer();
        customer.setId(1L);
        customer.setFullName("John Doe");
        customer.setInsuranceTypes(Arrays.asList("CAR", "HEALTH"));

        insuranceClaim = new InsuranceClaim();
        insuranceClaim.setId(1L);
        insuranceClaim.setClaimType(ClaimType.CAR);
        insuranceClaim.setCustomer(customer);
    }

    @Test
    void createClaim_Success() {
        when(customerService.getCustomer(claimDto.getCustomerId())).thenReturn(Optional.of(customer));
        when(objectMapper.convertValue(claimDto, InsuranceClaim.class)).thenReturn(insuranceClaim);
        when(insuranceClaimRepository.save(any(InsuranceClaim.class))).thenReturn(insuranceClaim);

        InsuranceClaim result = insuranceClaimService.createClaim(claimDto);

        assertNotNull(result);
        assertEquals("CAR", result.getClaimType().toString());
        assertEquals(customer, result.getCustomer());
        verify(insuranceClaimRepository, times(1)).save(insuranceClaim);
    }

    @Test
    void createClaim_CustomerNotFound() {
        when(customerService.getCustomer(claimDto.getCustomerId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            insuranceClaimService.createClaim(claimDto);
        });

        assertEquals("Customer not found", exception.getMessage());
        verify(insuranceClaimRepository, never()).save(any(InsuranceClaim.class));
    }

    @Test
    void createClaim_ClaimNotEligible() {
        customer.setInsuranceTypes(Arrays.asList("HEALTH"));  // Customer doesn't have "Car" insurance
        when(customerService.getCustomer(claimDto.getCustomerId())).thenReturn(Optional.of(customer));

        Exception exception = assertThrows(ClaimNotProcessedException.class, () -> {
            insuranceClaimService.createClaim(claimDto);
        });

        assertEquals("Customer is not eligible for registring this claim", exception.getMessage());
        verify(insuranceClaimRepository, never()).save(any(InsuranceClaim.class));
    }

    @Test
    void updateClaimStatus_Success() {
        when(insuranceClaimRepository.findById(insuranceClaim.getId())).thenReturn(Optional.of(insuranceClaim));
        when(insuranceClaimRepository.save(any(InsuranceClaim.class))).thenReturn(insuranceClaim);

        InsuranceClaim result = insuranceClaimService.updateClaimStatus(insuranceClaim);

        assertNotNull(result);
        assertEquals("CAR", result.getClaimType().toString());
        verify(insuranceClaimRepository, times(1)).save(insuranceClaim);
    }

    @Test
    void updateClaimStatus_ClaimNotFound() {
        when(insuranceClaimRepository.findById(insuranceClaim.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ClaimNotProcessedException.class, () -> {
            insuranceClaimService.updateClaimStatus(insuranceClaim);
        });

        assertEquals("Claim not found", exception.getMessage());
        verify(insuranceClaimRepository, never()).save(any(InsuranceClaim.class));
    }

    @Test
    void getClaimsByCustomerId_ReturnsList() {
        when(insuranceClaimRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(insuranceClaim));

        List<InsuranceClaim> result = insuranceClaimService.getClaimsByCustomerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CAR", result.get(0).getClaimType().toString());
        verify(insuranceClaimRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void deleteClaim_Success() {
        when(insuranceClaimRepository.findById(1L)).thenReturn(Optional.of(insuranceClaim));

        insuranceClaimService.deleteClaim(1L);

        verify(insuranceClaimRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteClaim_ClaimNotFound() {
        when(insuranceClaimRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            insuranceClaimService.deleteClaim(1L);
        });

        assertEquals("Claim not found", exception.getMessage());
        verify(insuranceClaimRepository, never()).deleteById(1L);
    }
}
