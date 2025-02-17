package com.crimsonlogic.onlinebiddingsystem.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crimsonlogic.onlinebiddingsystem.entity.ProductCategory;

@Repository
public interface CategoryRepository extends JpaRepository<ProductCategory, Long> {
	
}
