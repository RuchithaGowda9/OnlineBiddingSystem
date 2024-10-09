package com.crimsonlogic.onlinebiddingsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserInfoTest {

    private UserInfo userInfo;

    @BeforeEach
    void setUp() {
        userInfo = new UserInfo();
        userInfo.setFirstName("John");
        userInfo.setLastName("Doe");
        userInfo.setPhoneNumber("1234567890");
        userInfo.setUser(new User(1L, "john@example.com", "password"));
    }

    @Test
    void testUserInfoCreation() {
        // Act
        // No ID generation to call here

        // Assert
        assertNotNull(userInfo.getFirstName());
        assertEquals("John", userInfo.getFirstName());
        assertNotNull(userInfo.getLastName());
        assertEquals("Doe", userInfo.getLastName());
        assertNotNull(userInfo.getPhoneNumber());
        assertEquals("1234567890", userInfo.getPhoneNumber());
        assertNotNull(userInfo.getUser());
        assertEquals("john@example.com", userInfo.getUser().getEmail());
    }

    @Test
    void testUserInfoFirstNameUpdate() {
        // Arrange
        userInfo.setFirstName("Jane");

        // Act
        // No specific action needed for this test

        // Assert
        assertEquals("Jane", userInfo.getFirstName());
    }

    @Test
    void testUserInfoLastNameUpdate() {
        // Arrange
        userInfo.setLastName("Smith");

        // Act
        // No specific action needed for this test

        // Assert
        assertEquals("Smith", userInfo.getLastName());
    }

    @Test
    void testUserInfoPhoneNumberUpdate() {
        // Arrange
        userInfo.setPhoneNumber("0987654321");

        // Act
        // No specific action needed for this test

        // Assert
        assertEquals("0987654321", userInfo.getPhoneNumber());
    }
}
