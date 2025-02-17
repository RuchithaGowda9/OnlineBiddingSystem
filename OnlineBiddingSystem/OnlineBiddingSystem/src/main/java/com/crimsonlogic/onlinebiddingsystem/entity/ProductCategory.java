package com.crimsonlogic.onlinebiddingsystem.entity;

import com.crimsonlogic.onlinebiddingsystem.util.IdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "product_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {

	@Id
	private Long categoryId;
	
	@Column(name = "category_name",length = 50,unique = true, nullable = false)
    private String categoryName;
	
	@Column(name = "category_description", length = 255, nullable = false)
	private String categoryDescription;
	
	@Column(name = "status", length = 20, nullable = false)
	private String status = "active";
	
	@PrePersist
	public void generateId() {
		this.categoryId = (Long) new IdGenerator().generate(null, this);
	}

}
