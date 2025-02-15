package com.insurance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.dto.request.InsuranceClaimDto;
import com.insurance.exception.ClaimNotProcessedException;
import com.insurance.exception.CustomerNotFoundException;
import com.insurance.model.Customer;
import com.insurance.model.InsuranceClaim;
import com.insurance.repository.InsuranceClaimRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsuranceClaimService {
    
    private static final Logger logger = LoggerFactory.getLogger(InsuranceClaimService.class);

    private final InsuranceClaimRepository insuranceClaimRepository;
    private final CustomerService customerService;
    private final ObjectMapper objectMapper;

    public InsuranceClaimService(InsuranceClaimRepository insuranceClaimRepository, CustomerService customerService,
                                 ObjectMapper objectMapper) {
        this.insuranceClaimRepository = insuranceClaimRepository;
        this.customerService = customerService;
        this.objectMapper = objectMapper;
    }

    public InsuranceClaim createClaim(InsuranceClaimDto claimDto) {
        logger.info("Creating a new insurance claim for customerId: {}", claimDto.getCustomerId());
        
        // Check if the provided customer exists in the database
        Customer customer = customerService.getCustomer(claimDto.getCustomerId())
                .orElseThrow(() -> {
                    logger.error("Customer with ID {} not found", claimDto.getCustomerId());
                    return new CustomerNotFoundException("Customer not found");
                });

        // Check if the customer has rights to raise that particular claim
        if (!customer.getInsuranceTypes().contains(claimDto.getClaimType())) {
            logger.warn("Customer with ID {} is not eligible for claim type: {}", claimDto.getCustomerId(), claimDto.getClaimType());
            throw new ClaimNotProcessedException("Customer is not eligible for registring this claim");
        }

        InsuranceClaim claim = objectMapper.convertValue(claimDto, InsuranceClaim.class);
        claim.setCustomer(customer);
        InsuranceClaim savedClaim = insuranceClaimRepository.save(claim);
        
        logger.info("Insurance claim created successfully with ID: {}", savedClaim.getId());
        return savedClaim;
    }

    public InsuranceClaim updateClaimStatus(InsuranceClaim claim) {
        logger.info("Updating status for claim ID: {}", claim.getId());

        InsuranceClaim persistedClaim = getClaimById(claim.getId())
                .orElseThrow(() -> {
                    logger.error("Claim with ID {} not found", claim.getId());
                    return new ClaimNotProcessedException("Claim not found");
                });

        persistedClaim.setStatus(claim.getStatus());
        InsuranceClaim updatedClaim = insuranceClaimRepository.save(persistedClaim);
        
        logger.info("Claim status updated successfully for claim ID: {}", updatedClaim.getId());
        return updatedClaim;
    }

    public List<InsuranceClaim> getClaimsByCustomerId(Long customerId) {
        logger.info("Fetching claims for customer ID: {}", customerId);
        List<InsuranceClaim> claims = insuranceClaimRepository.findByCustomerId(customerId);
        logger.info("Found {} claims for customer ID: {}", claims.size(), customerId);
        return claims;
    }

    public void deleteClaim(Long claimId) {
        logger.info("Deleting claim with ID: {}", claimId);
        
        getClaimById(claimId).orElseThrow(() -> {
            logger.error("Claim with ID {} not found", claimId);
            return new CustomerNotFoundException("Claim not found");
        });

        insuranceClaimRepository.deleteById(claimId);
        logger.info("Claim with ID {} deleted successfully", claimId);
    }

    public Optional<InsuranceClaim> getClaimById(Long id) {
        logger.info("Fetching claim with ID: {}", id);
        Optional<InsuranceClaim> claim = insuranceClaimRepository.findById(id);
        if (claim.isPresent()) {
            logger.info("Claim found with ID: {}", id);
        } else {
            logger.warn("Claim with ID {} not found", id);
        }
        return claim;
    }
}
