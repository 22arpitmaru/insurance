package com.insurance.controller;

import com.insurance.dto.request.InsuranceClaimDto;
import com.insurance.model.InsuranceClaim;
import com.insurance.model.enums.ClaimType;
import com.insurance.service.InsuranceClaimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsuranceClaimControllerTest {

    @InjectMocks
    private InsuranceClaimController insuranceClaimController;

    @Mock
    private InsuranceClaimService claimService;

    private InsuranceClaim insuranceClaim;
    private InsuranceClaimDto claimDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        claimDto = new InsuranceClaimDto();
        claimDto.setCustomerId(1L);
        claimDto.setClaimType("CAR");

        insuranceClaim = new InsuranceClaim();
        insuranceClaim.setId(1L);
        insuranceClaim.setClaimType(ClaimType.CAR);
    }

    @Test
    void createClaim_Success() {
        when(claimService.createClaim(any(InsuranceClaimDto.class))).thenReturn(insuranceClaim);

        ResponseEntity<InsuranceClaim> response = insuranceClaimController.createClaim(claimDto);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(ClaimType.CAR, response.getBody().getClaimType());
        verify(claimService, times(1)).createClaim(any(InsuranceClaimDto.class));
    }

    @Test
    void getClaimsByCustomerId_Success() {
        List<InsuranceClaim> claims = Arrays.asList(insuranceClaim);
        when(claimService.getClaimsByCustomerId(1L)).thenReturn(claims);

        ResponseEntity<List<InsuranceClaim>> response = insuranceClaimController.getClaimsByCustomerId(1L);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(claimService, times(1)).getClaimsByCustomerId(1L);
    }

    @Test
    void updateClaimStatus_Success() {
        when(claimService.updateClaimStatus(any(InsuranceClaim.class))).thenReturn(insuranceClaim);

        ResponseEntity<InsuranceClaim> response = insuranceClaimController.updateClaimStatus(1L, insuranceClaim);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(claimService, times(1)).updateClaimStatus(any(InsuranceClaim.class));
    }

    @Test
    void deleteClaim_Success() {
        doNothing().when(claimService).deleteClaim(1L);

        ResponseEntity<Void> response = insuranceClaimController.deleteClaim(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(claimService, times(1)).deleteClaim(1L);
    }
}
