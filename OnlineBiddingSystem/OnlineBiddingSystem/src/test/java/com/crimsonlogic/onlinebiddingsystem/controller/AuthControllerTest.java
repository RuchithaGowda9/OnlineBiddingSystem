package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.dto.ChangePasswordDto;
import com.crimsonlogic.onlinebiddingsystem.dto.RegistrationDto;
import com.crimsonlogic.onlinebiddingsystem.dto.UserDto;
import com.crimsonlogic.onlinebiddingsystem.dto.UserInfoDto;
import com.crimsonlogic.onlinebiddingsystem.entity.Role;
import com.crimsonlogic.onlinebiddingsystem.entity.RoleName;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;
import com.crimsonlogic.onlinebiddingsystem.repository.RoleRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import com.crimsonlogic.onlinebiddingsystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private HttpSession session;

    private RegistrationDto registrationDto;
    private User user;
    private UserInfo userInfo;
    private UserDto userDto;
    private UserInfoDto userInfoDto;
    private ChangePasswordDto changePasswordDto;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize User
        user = new User();
        user.setEmail("test@example.com");

        // Initialize Role
        role = new Role();
        role.setRoleId(1L); // Assign an ID to the role
        //role.setName("USER"); // Assuming there's a name property

        // Initialize UserInfo
        userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setFirstName("John");
        userInfo.setLastName("Doe");
        userInfo.setPhoneNumber("1234567890");
        userInfo.setRole(role); // Set the role directly

        // Initialize DTOs
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");

        userInfoDto = new UserInfoDto();
        userInfoDto.setFirstName("John");
        userInfoDto.setLastName("Doe");
        userInfoDto.setPhoneNumber("1234567890");
        userInfoDto.setRoleName(RoleName.CUSTOMER); // Use enum directly

        registrationDto = new RegistrationDto();
        registrationDto.setUser(userDto); // Adjusted to UserDto
        registrationDto.setUserInfo(userInfoDto); // Adjusted to UserInfoDto

        changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setCurrentPassword("oldPassword");
        changePasswordDto.setNewPassword("newPassword");
    }



    @Test
    void testGetUserInfo_Success() {
        // Arrange
        when(session.getAttribute("userInfo")).thenReturn(userInfoDto);
        when(session.getAttribute("user")).thenReturn(userDto);

        // Act
        ResponseEntity<Map<String, Object>> response = authController.getUserInfo(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userInfoDto, response.getBody().get("userInfo"));
        assertEquals(userDto, response.getBody().get("user"));
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        when(session.getAttribute("user")).thenReturn(userDto);
        when(authService.validatePassword(any(), any())).thenReturn(true);

        // Act
        ResponseEntity<String> response = authController.changePassword(changePasswordDto, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());
        verify(authService, times(1)).updatePassword(user.getEmail(), changePasswordDto.getNewPassword());
    }

    @Test
    void testGetWalletBalance_Success() {
        // Arrange
        when(session.getAttribute("user")).thenReturn(userDto);
        when(authService.getWalletBalance(user.getEmail())).thenReturn(100.0);

        // Act
        ResponseEntity<Double> response = authController.getWalletBalance(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.0, response.getBody());
    }

    @Test
    void testRechargeWallet_Success() {
        // Arrange
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 50.0);
        when(session.getAttribute("user")).thenReturn(userDto);

        // Act
        ResponseEntity<String> response = authController.rechargeWallet(requestBody, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Wallet recharged successfully!", response.getBody());
        verify(authService, times(1)).rechargeWallet(user.getEmail(), 50.0);
    }

    @Test
    void testLogout_Success() {
        // Act
        ResponseEntity<String> response = authController.logout(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully", response.getBody());
        verify(session, times(1)).invalidate();
    }
}
