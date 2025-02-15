package com.insurance.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Getter
@Setter
public class Customer  implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name= "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "FULL_NAME")
    private String fullName;

    @Column(name= "DOB")
    private Date dateOfBirth;

    @Column(name= "INSURANCE_TYPE")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> insuranceTypes;

}