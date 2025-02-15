package com.insurance.dto.request;

import java.util.Date;

import com.insurance.model.enums.ClaimStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class InsuranceClaimDto {

	private Long customerId;

	private String claimType;

	private Date claimDate;

	private ClaimStatus status;

	private double cost;

}
