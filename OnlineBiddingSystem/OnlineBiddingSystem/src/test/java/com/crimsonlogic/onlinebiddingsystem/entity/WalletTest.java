package com.crimsonlogic.onlinebiddingsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setUser(new User(1L, "john@example.com", "password"));
        wallet.setBalance(100.0);
    }

    @Test
    void testWalletCreation() {
        // Act
        // No ID generation to call here

        // Assert
        assertNotNull(wallet.getUser());
        assertEquals("john@example.com", wallet.getUser().getEmail());
        assertEquals(100.0, wallet.getBalance());
    }

    @Test
    void testBalanceUpdate() {
        // Arrange
        wallet.setBalance(200.0);

        // Act
        // No specific action needed for this test

        // Assert
        assertEquals(200.0, wallet.getBalance());
    }

    @Test
    void testUserUpdate() {
        // Arrange
        User newUser = new User(2L, "jane@example.com", "newpassword");
        wallet.setUser(newUser);

        // Act
        // No specific action needed for this test

        // Assert
        assertNotNull(wallet.getUser());
        assertEquals("jane@example.com", wallet.getUser().getEmail());
    }

    @Test
    void testInitialBalance() {
        // Act
        wallet.generateId(); // Simulating the pre-persist action

        // Assert
        assertEquals(0.0, wallet.getBalance()); // Check that balance is initialized to 0.0
    }
}
