package com.crimsonlogic.onlinebiddingsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeliveryAgentDetailsTest {

    private DeliveryAgentDetails deliveryAgentDetails;
    private User user;

    @BeforeEach
    void setUp() {
        deliveryAgentDetails = new DeliveryAgentDetails();
        user = new User(); // Assuming User has a no-arg constructor

        // Set up sample data
        user.setUserId(1L); // Assuming User has setUserId method
        deliveryAgentDetails.setUser(user);
        deliveryAgentDetails.setDeliveryAgencyName("Fast Delivery");
    }

    @Test
    void testDeliveryAgentDetailsCreation() {
        // Act
        // No ID generation to call here

        // Assert
        assertNotNull(deliveryAgentDetails.getUser());
        assertEquals("Fast Delivery", deliveryAgentDetails.getDeliveryAgencyName());
    }

    @Test
    void testDeliveryAgentDetailsProperties() {
        // Assert initial properties
        assertEquals(user, deliveryAgentDetails.getUser());
        assertEquals("Fast Delivery", deliveryAgentDetails.getDeliveryAgencyName());
    }

    @Test
    void testDeliveryAgencyNameUpdate() {
        // Arrange
        String newAgencyName = "Express Delivery";

        // Act
        deliveryAgentDetails.setDeliveryAgencyName(newAgencyName);

        // Assert
        assertEquals(newAgencyName, deliveryAgentDetails.getDeliveryAgencyName());
    }
}
