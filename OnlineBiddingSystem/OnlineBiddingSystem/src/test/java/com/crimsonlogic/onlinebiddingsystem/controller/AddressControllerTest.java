package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.entity.Address;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.service.AddressService;
import com.crimsonlogic.onlinebiddingsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddressControllerTest {

    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressService addressService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addAddress_UserExists_ShouldReturnSavedAddress() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        Address address = new Address();
        address.setUser(user);
        Address savedAddress = new Address(); // Mock saved Address object

        when(userService.findById(user.getUserId())).thenReturn(user);
        when(addressService.addAddress(any(Address.class))).thenReturn(savedAddress);

        // Act
        Address response = addressController.addAddress(address);

        // Assert
        assertEquals(savedAddress, response);
        verify(userService, times(1)).findById(user.getUserId());
        verify(addressService, times(1)).addAddress(address);
    }

    @Test
    void addAddress_UserNotFound_ShouldThrowException() {
        // Arrange
        Address address = new Address();
        User user = new User();
        user.setUserId(1L);
        address.setUser(user);

        when(userService.findById(user.getUserId())).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            addressController.addAddress(address);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userService, times(1)).findById(user.getUserId());
        verify(addressService, never()).addAddress(any(Address.class));
    }

    @Test
    void getUserAddresses_ShouldReturnAddressList() {
        // Arrange
        Long userId = 1L;
        Address address1 = new Address(); // Mock first Address
        Address address2 = new Address(); // Mock second Address
        List<Address> expectedAddresses = List.of(address1, address2);

        when(addressService.getAllAddressesByUserId(userId)).thenReturn(expectedAddresses);

        // Act
        List<Address> response = addressController.getUserAddresses(userId);

        // Assert
        assertEquals(expectedAddresses, response);
        verify(addressService, times(1)).getAllAddressesByUserId(userId);
    }

    @Test
    void getUserAddresses_NoAddresses_ShouldReturnEmptyList() {
        // Arrange
        Long userId = 1L;
        when(addressService.getAllAddressesByUserId(userId)).thenReturn(Collections.emptyList());

        // Act
        List<Address> response = addressController.getUserAddresses(userId);

        // Assert
        assertTrue(response.isEmpty());
        verify(addressService, times(1)).getAllAddressesByUserId(userId);
    }
}
