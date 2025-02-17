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
@Table(name = "delivery_agent_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAgentDetails {
	
	@Id
	private Long deliveryAgentDetailsId;
	
	@ManyToOne
    @JoinColumn(name = "delivery_agent_id")
    private User user;

	@Column(name = "delivery_agency_name", length = 20)
    private String deliveryAgencyName;
    
    @PrePersist
	public void generateId() {
		this.deliveryAgentDetailsId = (Long) new IdGenerator().generate(null, this);
	}
	
}
