package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.entity.DeliveryAgentDetails;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;
import com.crimsonlogic.onlinebiddingsystem.repository.DeliveryAgentDetailsRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeliveryAgentDetailsServiceImplTest {

    @InjectMocks
    private DeliveryAgentDetailsServiceImpl deliveryAgentService;

    @Mock
    private DeliveryAgentDetailsRepository deliveryAgentRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    private DeliveryAgentDetails deliveryAgent;
    private User user;
    private UserInfo userInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize User
        user = new User();
        user.setUserId(1L); // Assuming userId is of type Long

        // Initialize UserInfo
        userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setPhoneNumber("1234567890");

        // Initialize DeliveryAgentDetails
        deliveryAgent = new DeliveryAgentDetails();
        deliveryAgent.setUser(user);
        deliveryAgent.setDeliveryAgencyName("Agency A");
    }

    @Test
    void testFindDeliveryPersonsByAgencyName_Success() {
        // Arrange
        List<DeliveryAgentDetails> agents = new ArrayList<>();
        agents.add(deliveryAgent);
        
        when(deliveryAgentRepository.findByDeliveryAgencyName("Agency A")).thenReturn(agents);
        when(userInfoRepository.findByUser(user)).thenReturn(Optional.of(userInfo));

        // Act
        List<UserInfo> result = deliveryAgentService.findDeliveryPersonsByAgencyName("Agency A");

        // Assert
        assertEquals(1, result.size());
        assertEquals(userInfo.getPhoneNumber(), result.get(0).getPhoneNumber());
        verify(deliveryAgentRepository, times(1)).findByDeliveryAgencyName("Agency A");
        verify(userInfoRepository, times(1)).findByUser(user);
    }

    @Test
    void testFindDeliveryPersonsByAgencyName_NoDeliveryAgents() {
        // Arrange
        when(deliveryAgentRepository.findByDeliveryAgencyName("Agency B")).thenReturn(new ArrayList<>());

        // Act
        List<UserInfo> result = deliveryAgentService.findDeliveryPersonsByAgencyName("Agency B");

        // Assert
        assertTrue(result.isEmpty());
        verify(deliveryAgentRepository, times(1)).findByDeliveryAgencyName("Agency B");
    }

    @Test
    void testFindAllDeliveryAgencies_Success() {
        // Arrange
        List<DeliveryAgentDetails> agents = new ArrayList<>();
        agents.add(deliveryAgent);
        
        when(deliveryAgentRepository.findAll()).thenReturn(agents);

        // Act
        List<String> result = deliveryAgentService.findAllDeliveryAgencies();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Agency A", result.get(0));
        verify(deliveryAgentRepository, times(1)).findAll();
    }

    @Test
    void testFindAllDeliveryAgencies_NoAgencies() {
        // Arrange
        when(deliveryAgentRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<String> result = deliveryAgentService.findAllDeliveryAgencies();

        // Assert
        assertTrue(result.isEmpty());
        verify(deliveryAgentRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_Success() {
        // Arrange
        List<DeliveryAgentDetails> agents = new ArrayList<>();
        agents.add(deliveryAgent);
        
        when(deliveryAgentRepository.findAll()).thenReturn(agents);

        // Act
        List<DeliveryAgentDetails> result = deliveryAgentService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Agency A", result.get(0).getDeliveryAgencyName());
        verify(deliveryAgentRepository, times(1)).findAll();
    }
}
