package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;
import com.crimsonlogic.onlinebiddingsystem.service.DeliveryAgentDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DeliveryAgentDetailsControllerTest {

    @InjectMocks
    private DeliveryAgentDetailsController deliveryAgentDetailsController;

    @Mock
    private DeliveryAgentDetailsService deliveryAgentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDeliveryPersons_Success() {
        // Arrange
        String agencyName = "Test Agency";
        UserInfo user1 = new UserInfo(/* initialize with necessary fields */);
        UserInfo user2 = new UserInfo(/* initialize with necessary fields */);
        List<UserInfo> deliveryPersons = Arrays.asList(user1, user2);

        when(deliveryAgentService.findDeliveryPersonsByAgencyName(agencyName)).thenReturn(deliveryPersons);

        // Act
        ResponseEntity<List<UserInfo>> response = deliveryAgentDetailsController.getDeliveryPersons(agencyName);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(deliveryPersons, response.getBody());
        verify(deliveryAgentService, times(1)).findDeliveryPersonsByAgencyName(agencyName);
    }

    @Test
    void testGetAllDeliveryAgencies_Success() {
        // Arrange
        List<String> agencies = Arrays.asList("Agency 1", "Agency 2");

        when(deliveryAgentService.findAllDeliveryAgencies()).thenReturn(agencies);

        // Act
        ResponseEntity<List<String>> response = deliveryAgentDetailsController.getAllDeliveryAgencies();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(agencies, response.getBody());
        verify(deliveryAgentService, times(1)).findAllDeliveryAgencies();
    }

    @Test
    void testGetDeliveryPersons_AgencyNotFound() {
        // Arrange
        String agencyName = "Nonexistent Agency";
        when(deliveryAgentService.findDeliveryPersonsByAgencyName(agencyName)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<UserInfo>> response = deliveryAgentDetailsController.getDeliveryPersons(agencyName);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Arrays.asList(), response.getBody());
        verify(deliveryAgentService, times(1)).findDeliveryPersonsByAgencyName(agencyName);
    }
}
