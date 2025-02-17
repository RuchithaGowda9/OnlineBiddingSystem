package com.crimsonlogic.onlinebiddingsystem.entity;

import java.time.LocalDateTime;

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
@Table(name = "bid")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

	@Id
	private Long bidId;
	
	@ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
	
	@ManyToOne
    @JoinColumn(name = "bidder_id")
    private User bidderId;
	
	@Column(name = "bid_price", nullable = false)
	private Double bidPrice;
	
	@Column(name = "bid_time")
	private LocalDateTime bidTime;
	
	@PrePersist
	public void generateId() {
		this.bidId = (Long) new IdGenerator().generate(null, this);
	}
	
	
}
