package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.dto.UserDto;
import com.crimsonlogic.onlinebiddingsystem.entity.Order;
import com.crimsonlogic.onlinebiddingsystem.entity.Product;
import com.crimsonlogic.onlinebiddingsystem.entity.ProductCategory;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.onlinebiddingsystem.repository.OrderRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.ProductRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import com.crimsonlogic.onlinebiddingsystem.service.AuthService;
import com.crimsonlogic.onlinebiddingsystem.service.OrderService;
import com.crimsonlogic.onlinebiddingsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    private User seller;
    private User buyer;
    private User deliveryPerson;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        seller = new User();
        seller.setUserId(1L);
        seller.setEmail("seller@example.com");
        seller.setPassword("password");

        buyer = new User();
        buyer.setUserId(2L);
        buyer.setEmail("buyer@example.com");
        buyer.setPassword("password");

        deliveryPerson = new User();
        deliveryPerson.setUserId(3L);
        deliveryPerson.setEmail("delivery@example.com");
        deliveryPerson.setPassword("password");

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setProductDescription("Description");
        ProductCategory category = new ProductCategory(); 
        product.setCategory(category);
        product.setAskingPrice(100.0);
        product.setProductStatus("available");

        order = new Order();
        order.setOrderId(1L); // Set a mock ID
        order.setProduct(product);
        order.setBuyerId(buyer);
        order.setSellerId(seller);
        order.setDeliveryPersonId(deliveryPerson);
        order.setOrderStatus("pending");
        order.setBidAmount(100.0f);
    }

    @Test
    void testGetDeliveryOrders_Success1() throws ResourceNotFoundException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(deliveryPerson.getUserId());
        when(session.getAttribute("user")).thenReturn(userDto);
        
        List<Order> orders = Arrays.asList(order);
        when(orderService.getOrdersByDeliveryPerson(any())).thenReturn(orders);
        when(userService.getUserById(buyer.getUserId())).thenReturn(buyer);
        when(userService.getUserById(seller.getUserId())).thenReturn(seller);

        // Act
        List<Order> response = orderController.getDeliveryOrders(session);

        // Assert
        assertEquals(orders, response);
    }


    @Test
    void testGetAllOrders_Success() {
        // Arrange
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orders, response.getBody());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderHistory_Unauthorized() {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrderHistory(session);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testGetOrderHistory_Success() {
        // Arrange
        UserDto userDto = new UserDto(); // Set appropriate fields
        userDto.setId(buyer.getUserId());
        when(session.getAttribute("user")).thenReturn(userDto);
        List<Order> orders = Arrays.asList(order);
        when(orderService.getOrdersByBuyerId(userDto.getId())).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrderHistory(session);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orders, response.getBody());
        verify(orderService, times(1)).getOrdersByBuyerId(userDto.getId());
    }

    @Test
    void testGetDeliveryOrders_Unauthorized() {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderController.getDeliveryOrders(session);
        });
    }

    @Test
    void testGetDeliveryOrders_Success() throws ResourceNotFoundException {
        // Arrange
        UserDto userDto = new UserDto(); // Set appropriate fields
        userDto.setId(deliveryPerson.getUserId());
        when(session.getAttribute("user")).thenReturn(userDto);
        List<Order> orders = Arrays.asList(order);
        when(orderService.getOrdersByDeliveryPerson(any())).thenReturn(orders);
        when(userService.getUserById(buyer.getUserId())).thenReturn(buyer);
        when(userService.getUserById(seller.getUserId())).thenReturn(seller);

        // Act
        List<Order> response = orderController.getDeliveryOrders(session);

        // Assert
        assertEquals(orders, response);
    }

    @Test
    void testUpdateOrderStatus_Success() throws ResourceNotFoundException {
        // Arrange
        Long orderId = order.getOrderId();
        String newStatus = "Delivered";
        HashMap<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("orderStatus", newStatus);

        when(orderService.getOrderById(orderId)).thenReturn(order);

        // Act
        ResponseEntity<Order> response = orderController.updateOrderStatus(orderId, statusUpdate);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(newStatus, response.getBody().getOrderStatus());
        verify(orderService, times(1)).updateOrder(order);
    }
}
