package com.mintyn.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Customer {

	@Id
    @GeneratedValue
	private Integer customerId; 
	@Column(name = "CUSTOMER_NAME")
    @NotBlank
    private String customerName;
	
	@NotNull
   @Column(name = "PHONE_NUMBER")
	private Long phoneNumber;
	
	@OneToOne(mappedBy = "customer", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Order order;
}
