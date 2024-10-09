package com.crimsonlogic.onlinebiddingsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderTest {

    private Order order;
    private Product product;
    private User buyer;
    private User seller;
    private User deliveryPerson;

    @BeforeEach
    void setUp() {
        order = new Order();

        // Initialize User and Product instances
        product = new Product(); // Assuming Product has a no-arg constructor
        buyer = new User(); // Assuming User has a no-arg constructor
        seller = new User(); // Assuming User has a no-arg constructor
        deliveryPerson = new User(); // Assuming User has a no-arg constructor

        // Set up sample data
        product.setProductId(1L); // Assuming Product has setProductId method
        buyer.setUserId(2L); // Assuming User has setUserId method
        seller.setUserId(3L); // Assuming User has setUserId method
        deliveryPerson.setUserId(4L); // Assuming User has setUserId method

        order.setProduct(product);
        order.setBuyerId(buyer);
        order.setSellerId(seller);
        order.setDeliveryPersonId(deliveryPerson);
        order.setOrderStatus("Pending");
        order.setBidAmount(150.0f);
    }

    @Test
    void testOrderCreation() {
        // Act
        // No ID generation to call here

        // Assert
        assertNotNull(order.getProduct());
        assertNotNull(order.getBuyerId());
        assertNotNull(order.getSellerId());
        assertNotNull(order.getDeliveryPersonId());
        assertEquals("Pending", order.getOrderStatus());
        assertEquals(150.0f, order.getBidAmount());
    }

    @Test
    void testOrderProperties() {
        // Assert initial properties
        assertEquals(product, order.getProduct());
        assertEquals(buyer, order.getBuyerId());
        assertEquals(seller, order.getSellerId());
        assertEquals(deliveryPerson, order.getDeliveryPersonId());
        assertEquals("Pending", order.getOrderStatus());
        assertEquals(150.0f, order.getBidAmount());
    }

    @Test
    void testOrderStatusUpdate() {
        // Arrange
        String newStatus = "Completed";

        // Act
        order.setOrderStatus(newStatus);

        // Assert
        assertEquals(newStatus, order.getOrderStatus());
    }

    @Test
    void testBidAmountUpdate() {
        // Arrange
        Float newBidAmount = 200.0f;

        // Act
        order.setBidAmount(newBidAmount);

        // Assert
        assertEquals(newBidAmount, order.getBidAmount());
    }
}
