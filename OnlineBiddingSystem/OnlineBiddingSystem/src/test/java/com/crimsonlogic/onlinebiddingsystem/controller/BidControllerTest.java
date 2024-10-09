package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.entity.Bid;
import com.crimsonlogic.onlinebiddingsystem.service.BidService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BidControllerTest {

    @InjectMocks
    private BidController bidController;

    @Mock
    private BidService bidService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeBid_ShouldReturnSavedBid() {
        // Arrange
        Long productId = 1L;
        Bid bid = new Bid(); // Assume Bid has required fields and constructor
        Bid savedBid = new Bid(); // Mock saved Bid object with expected values

        when(bidService.placeBid(eq(productId), any(Bid.class))).thenReturn(savedBid);

        // Act
        ResponseEntity<Bid> response = bidController.placeBid(productId, bid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedBid, response.getBody());
        verify(bidService, times(1)).placeBid(eq(productId), any(Bid.class));
    }

    @Test
    void getBidsByProduct_ShouldReturnBidsList() {
        // Arrange
        Long productId = 1L;
        Bid bid1 = new Bid(); // Mock first Bid
        Bid bid2 = new Bid(); // Mock second Bid
        List<Bid> expectedBids = List.of(bid1, bid2);

        when(bidService.getBidsByProductId(productId)).thenReturn(expectedBids);

        // Act
        ResponseEntity<List<Bid>> response = bidController.getBidsByProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBids, response.getBody());
        verify(bidService, times(1)).getBidsByProductId(productId);
    }

    @Test
    void getBidsByProduct_NoBidsFound_ShouldReturnEmptyList() {
        // Arrange
        Long productId = 1L;
        when(bidService.getBidsByProductId(productId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Bid>> response = bidController.getBidsByProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(bidService, times(1)).getBidsByProductId(productId);
    }
}
