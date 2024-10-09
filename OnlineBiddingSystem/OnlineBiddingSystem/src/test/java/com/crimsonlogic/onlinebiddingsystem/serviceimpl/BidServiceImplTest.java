package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.entity.Bid;
import com.crimsonlogic.onlinebiddingsystem.entity.Product;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.repository.BidRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.ProductRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BidServiceImplTest {

    @InjectMocks
    private BidServiceImpl bidService;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private Product product;
    private User user;
    private Bid bid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Product
        product = new Product();
        product.setProductId(1L);
        // Set other properties if needed

        // Initialize User
        user = new User();
        user.setUserId(1L);
        // Set other properties if needed

        // Initialize Bid
        bid = new Bid();
        bid.setBidId(1L);
        bid.setBidderId(user);
        // Set other properties if needed
    }

    @Test
    void testPlaceBid_Success() {
        // Arrange
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);

        // Act
        Bid result = bidService.placeBid(product.getProductId(), bid);

        // Assert
        assertNotNull(result);
        assertEquals(bid.getBidId(), result.getBidId());
        assertEquals(bid.getBidderId().getUserId(), result.getBidderId().getUserId());
        assertEquals(product.getProductId(), result.getProduct().getProductId());
        assertNotNull(result.getBidTime());
        verify(bidRepository, times(1)).save(bid);
    }

    @Test
    void testPlaceBid_ProductNotFound() {
        // Arrange
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bidService.placeBid(product.getProductId(), bid));
    }

    @Test
    void testPlaceBid_UserNotFound() {
        // Arrange
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bidService.placeBid(product.getProductId(), bid));
    }

    @Test
    void testGetBidsByProductId_Success() {
        // Arrange
        List<Bid> bids = new ArrayList<>();
        bids.add(bid);
        when(bidRepository.findByProduct_ProductId(product.getProductId())).thenReturn(bids);

        // Act
        List<Bid> result = bidService.getBidsByProductId(product.getProductId());

        // Assert
        assertEquals(1, result.size());
        assertEquals(bid.getBidId(), result.get(0).getBidId());
        verify(bidRepository, times(1)).findByProduct_ProductId(product.getProductId());
    }

    @Test
    void testGetBidsByProductId_NoBids() {
        // Arrange
        when(bidRepository.findByProduct_ProductId(product.getProductId())).thenReturn(new ArrayList<>());

        // Act
        List<Bid> result = bidService.getBidsByProductId(product.getProductId());

        // Assert
        assertTrue(result.isEmpty());
        verify(bidRepository, times(1)).findByProduct_ProductId(product.getProductId());
    }
}
