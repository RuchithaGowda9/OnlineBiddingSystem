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
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	@Id
	private Long productId;
	
	@Column(name = "product_name", length = 50)
	private String productName;
	
	@Column(name = "product_description", length = 50)
	private String productDescription;
	
	@ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;
	
	@Column(name = "asking_price", nullable = false)
	private Double askingPrice;
	
	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;
	
	@ManyToOne
    @JoinColumn(name = "seller_id")
	private User sellerId;
	
	@Column(name = "product_status", length = 20, nullable = false)
	private String productStatus;
	
	@Column(name = "image_path", length = 50)
	private String image;
	
	@PrePersist
	public void generateId() {
		this.productId = (Long) new IdGenerator().generate(null, this);
	}

	public Product(Long productId2) {
		
	}
	
}
