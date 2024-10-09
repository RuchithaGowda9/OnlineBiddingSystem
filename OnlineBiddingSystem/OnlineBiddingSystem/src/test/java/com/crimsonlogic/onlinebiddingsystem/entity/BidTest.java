package com.crimsonlogic.onlinebiddingsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BidTest {

    private Bid bid;
    private Product product;
    private User bidder;

    @BeforeEach
    void setUp() {
        bid = new Bid();
        product = new Product(); // Assuming Product has a no-arg constructor
        bidder = new User();     // Assuming User has a no-arg constructor

        // Set up sample data
        product.setProductId(1L); // Assuming Product has setProductId method
        bidder.setUserId(1L);     // Assuming User has setUserId method

        bid.setProduct(product);
        bid.setBidderId(bidder);
        bid.setBidPrice(100.0);
        bid.setBidTime(LocalDateTime.now());
    }

    @Test
    void testBidCreation() {
        // Act
        // No ID generation to call here

        // Assert
        assertNotNull(bid.getProduct());
        assertNotNull(bid.getBidderId());
        assertEquals(100.0, bid.getBidPrice());
        assertNotNull(bid.getBidTime());
    }

    @Test
    void testBidProperties() {
        // Assert initial properties
        assertEquals(product, bid.getProduct());
        assertEquals(bidder, bid.getBidderId());
        assertEquals(100.0, bid.getBidPrice());
        assertNotNull(bid.getBidTime());
    }

    @Test
    void testBidPriceUpdate() {
        // Arrange
        double newPrice = 150.0;

        // Act
        bid.setBidPrice(newPrice);

        // Assert
        assertEquals(newPrice, bid.getBidPrice());
    }
}
