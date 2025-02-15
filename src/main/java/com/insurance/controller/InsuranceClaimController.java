package com.insurance.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.insurance.dto.request.InsuranceClaimDto;
import com.insurance.model.InsuranceClaim;
import com.insurance.service.InsuranceClaimService;

@RestController
@RequestMapping("/api/v1/claims")
public class InsuranceClaimController {

    private static final Logger logger = LoggerFactory.getLogger(InsuranceClaimController.class);
    
    private final InsuranceClaimService claimService;

    public InsuranceClaimController(InsuranceClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    public ResponseEntity<InsuranceClaim> createClaim(@RequestBody InsuranceClaimDto claimDto) {
        logger.info("Received request to create a new claim for customerId: {}", claimDto.getCustomerId());
        InsuranceClaim createdClaim = claimService.createClaim(claimDto);
        logger.info("Claim created successfully with ID: {}", createdClaim.getId());
        return ResponseEntity.ok(createdClaim);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InsuranceClaim>> getClaimsByCustomerId(@PathVariable Long customerId) {
        logger.info("Received request to fetch claims for customer ID: {}", customerId);
        List<InsuranceClaim> claims = claimService.getClaimsByCustomerId(customerId);
        logger.info("Found {} claims for customer ID: {}", claims.size(), customerId);
        return ResponseEntity.ok(claims);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsuranceClaim> updateClaimStatus(@PathVariable Long id, @RequestBody InsuranceClaim claim) {
        logger.info("Received request to update claim status for claim ID: {}", id);
        InsuranceClaim updatedClaim = claimService.updateClaimStatus(claim);
        logger.info("Claim status updated successfully for claim ID: {}", updatedClaim.getId());
        return ResponseEntity.ok(updatedClaim);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        logger.info("Received request to delete claim with ID: {}", id);
        claimService.deleteClaim(id);
        logger.info("Claim with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
