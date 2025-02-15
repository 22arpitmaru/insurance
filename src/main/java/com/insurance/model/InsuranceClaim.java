package com.insurance.model;

import java.util.Date;

import com.insurance.model.enums.ClaimStatus;
import com.insurance.model.enums.ClaimType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class InsuranceClaim {

    @Id
    @Column(name= "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name= "CLAIM_TYPE")
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;

    @Column(name= "CLAIM_DATE")
    private Date claimDate;

    @Column(name= "STATUS")
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @Column(name= "COST")
    private double cost;

}