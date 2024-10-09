package com.crimsonlogic.onlinebiddingsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("securePassword123");
    }

    @Test
    void testUserCreation() {
        // Act
        // No ID generation to call here

        // Assert
        assertNotNull(user.getEmail());
        assertEquals("test@example.com", user.getEmail());
        assertNotNull(user.getPassword());
        assertEquals("securePassword123", user.getPassword());
    }

    @Test
    void testUserEmailUpdate() {
        // Arrange
        user.setEmail("new_email@example.com");

        // Act
        // No specific action needed for this test

        // Assert
        assertEquals("new_email@example.com", user.getEmail());
    }

    @Test
    void testUserPasswordUpdate() {
        // Arrange
        user.setPassword("newSecurePassword456");

        // Act
        // No specific action needed for this test

        // Assert
        assertEquals("newSecurePassword456", user.getPassword());
    }

    @Test
    void testUserIdSet() {
        // Arrange
        long testUserId = 1L;

        // Act
        user.setUserId(testUserId);

        // Assert
        assertEquals(testUserId, user.getUserId());
    }
}
