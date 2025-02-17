package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.entity.Address;
import com.crimsonlogic.onlinebiddingsystem.entity.User; // Assuming you have a User entity
import com.crimsonlogic.onlinebiddingsystem.repository.AddressRepository;
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

class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    private Address address;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize User
        user = new User();
        user.setUserId(1L); // Assuming userId is of type Long

        // Initialize Address
        address = new Address();
        address.setAddressId(1L);
        address.setUser(user);
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setState("IL");
        address.setPincode(62701L);
    }

    @Test
    void testAddAddress_Success() {
        // Arrange
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // Act
        Address result = addressService.addAddress(address);

        // Assert
        assertNotNull(result);
        assertEquals(address.getStreet(), result.getStreet());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testGetAllAddressesByUserId_Success() {
        // Arrange
        List<Address> addressList = new ArrayList<>();
        
        // Create addresses for user ID 1
        Address address1 = new Address();
        address1.setUser(user); // User ID 1
        Address address2 = new Address();
        address2.setUser(user); // User ID 1
        
        // Create an address for a different user
        Address address3 = new Address();
        User differentUser = new User();
        differentUser.setUserId(2L); // Different user ID
        address3.setUser(differentUser);

        addressList.add(address1);
        addressList.add(address2);
        addressList.add(address3);

        when(addressRepository.findAll()).thenReturn(addressList);

        // Act
        List<Address> result = addressService.getAllAddressesByUserId(1L);

        // Assert
        assertEquals(2, result.size()); // Should return 2 addresses for userId 1
        assertTrue(result.contains(address1));
        assertTrue(result.contains(address2));
        assertFalse(result.contains(address3));
        verify(addressRepository, times(1)).findAll();
    }


    @Test
    void testGetAllAddressesByUserId_NoAddresses() {
        // Arrange
        when(addressRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Address> result = addressService.getAllAddressesByUserId(1L);

        // Assert
        assertTrue(result.isEmpty());
        verify(addressRepository, times(1)).findAll();
    }
}
