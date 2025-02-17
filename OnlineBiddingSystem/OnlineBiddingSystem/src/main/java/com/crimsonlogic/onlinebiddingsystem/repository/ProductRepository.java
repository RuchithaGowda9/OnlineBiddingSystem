package com.crimsonlogic.onlinebiddingsystem.repository;

import com.crimsonlogic.onlinebiddingsystem.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findBySellerId_userId(Long sellerId);
   
}
