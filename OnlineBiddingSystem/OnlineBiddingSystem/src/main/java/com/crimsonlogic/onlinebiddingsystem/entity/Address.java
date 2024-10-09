package com.crimsonlogic.onlinebiddingsystem.entity;

import com.crimsonlogic.onlinebiddingsystem.util.IdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ruchitha
 *
 */
@Entity
@Table(name = "customer_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	@Id
	private Long addressId;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	@Column(name = "house_no", length = 3, nullable = false)
	private String houseNo;
	
	@Column(name = "street_name", length = 50)
    private String street;
	
	@Column(name = "apartment", length = 50)
    private String apartment;	
	
	@Column(name = "city", length = 50, nullable = false)
    private String city;
	
	@Column(name = "state", length = 50, nullable = false)
    private String state;
	
	@Column(name = "pincode", length = 6, nullable = false)
    private Long pincode;
	
	@PrePersist
	public void generateId() {
		this.addressId = (Long) new IdGenerator().generate(null, this);
	}
	
}
