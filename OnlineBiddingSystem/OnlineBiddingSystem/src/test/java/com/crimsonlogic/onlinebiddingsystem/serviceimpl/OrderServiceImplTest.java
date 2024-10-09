package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.entity.Order;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.onlinebiddingsystem.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    private Order order;
    private User buyer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Buyer
        buyer = new User();
        buyer.setUserId(1L);

        // Initialize Order
        order = new Order();
        order.setOrderId(1L);
        order.setBuyerId(buyer);
        // Add more properties as needed
    }

    @Test
    void testCreateOrder_Success() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order result = orderService.createOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.getOrderId(), result.getOrderId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetOrdersByBuyer_Success() {
        // Arrange
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findByBuyerId(buyer)).thenReturn(orderList);

        // Act
        List<Order> result = orderService.getOrdersByBuyer(buyer);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(order));
        verify(orderRepository, times(1)).findByBuyerId(buyer);
    }

    @Test
    void testGetAllOrders_Success() {
        // Arrange
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findAll()).thenReturn(orderList);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(order));
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrdersByBuyerId_Success() {
        // Arrange
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findByBuyerId(any(User.class))).thenReturn(orderList);

        // Act
        List<Order> result = orderService.getOrdersByBuyerId(1L);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(order));
        verify(orderRepository, times(1)).findByBuyerId(any(User.class));
    }

    @Test
    void testGetOrderById_Success() throws ResourceNotFoundException {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.getOrderById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(order.getOrderId(), result.getOrderId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderById_NotFound() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderById(1L);
        });

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void testUpdateOrder_Success() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order result = orderService.updateOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.getOrderId(), result.getOrderId());
        verify(orderRepository, times(1)).save(order);
    }
}
