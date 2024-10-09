package com.crimsonlogic.onlinebiddingsystem.entity;

import com.crimsonlogic.onlinebiddingsystem.util.IdGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddressTest {

    @InjectMocks
    private Address address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = new Address();
        address.setHouseNo("101");
        address.setStreet("Main St");
        address.setApartment("Apt 1");
        address.setCity("Cityville");
        address.setState("Stateland");
        address.setPincode(123456L);
    }

    @Test
    void testAddressCreation() {
        // Arrange
        address.setCity("Test City");
        address.setState("Test State");
        address.setPincode(654321L);

        // Act
        // No ID generation to call here

        // Assert
        assertEquals("Test City", address.getCity());
        assertEquals("Test State", address.getState());
        assertEquals(654321L, address.getPincode());
    }

    @Test
    void testAddressProperties() {
        // Assert initial properties
        assertEquals("101", address.getHouseNo());
        assertEquals("Main St", address.getStreet());
        assertEquals("Apt 1", address.getApartment());
        assertEquals("Cityville", address.getCity());
        assertEquals("Stateland", address.getState());
        assertEquals(123456L, address.getPincode());
    }
}
