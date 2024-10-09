package com.crimsonlogic.onlinebiddingsystem.service;

import java.util.List;

import com.crimsonlogic.onlinebiddingsystem.entity.Bid;

public interface BidService {

	Bid placeBid(Long productId, Bid bid); 

	List<Bid> getBidsByProductId(Long productId);

}
