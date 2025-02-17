package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.onlinebiddingsystem.entity.Bid;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.entity.Product;
import com.crimsonlogic.onlinebiddingsystem.repository.BidRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.ProductRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import com.crimsonlogic.onlinebiddingsystem.service.BidService;
@Service
public class BidServiceImpl implements BidService{

	 @Autowired
	    private BidRepository bidRepository;

	    @Autowired
	    private ProductRepository productRepository;

	    @Autowired
	    private UserRepository userRepository;

	    
	    @Override
	    public Bid placeBid(Long productId, Bid bid) {
	        Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new RuntimeException("Product not found"));  // Use the productId from path
	        User bidder = userRepository.findById(bid.getBidderId().getUserId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	        bid.setProduct(product); 
	        bid.setBidderId(bidder);
	        bid.setBidTime(LocalDateTime.now());
	        return bidRepository.save(bid);
	    }

	 	@Override
	 	public List<Bid> getBidsByProductId(Long productId) {
	        return bidRepository.findByProduct_ProductId(productId);
	    }
}
