package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.entity.Bid;
import com.crimsonlogic.onlinebiddingsystem.service.BidService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class BidController {

	@Autowired
	private BidService bidService;

	@PostMapping("/bids/{productId}")  
    public ResponseEntity<Bid> placeBid(@PathVariable Long productId, @RequestBody Bid bid) {
        Bid savedBid = bidService.placeBid(productId, bid); 
        return ResponseEntity.ok(savedBid);
    }
	
	@GetMapping("/bids/product/{productId}")
    public ResponseEntity<List<Bid>> getBidsByProduct(@PathVariable Long productId) {
        List<Bid> bids = bidService.getBidsByProductId(productId);
        return ResponseEntity.ok(bids);
    }

}
