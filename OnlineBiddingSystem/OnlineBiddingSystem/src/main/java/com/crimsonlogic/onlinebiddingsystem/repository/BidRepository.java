package com.crimsonlogic.onlinebiddingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.onlinebiddingsystem.entity.Bid;

public interface BidRepository extends JpaRepository<Bid, Long>{
	List<Bid> findByProduct_ProductId(Long productId);
}
