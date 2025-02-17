package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.entity.Role;
import com.crimsonlogic.onlinebiddingsystem.entity.RoleName;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;
import com.crimsonlogic.onlinebiddingsystem.entity.Wallet;
import com.crimsonlogic.onlinebiddingsystem.exception.UserAlreadyRegisteredException;
import com.crimsonlogic.onlinebiddingsystem.repository.RoleRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.WalletRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserInfoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private ModelMapper modelMapper;

    private User user;
    private UserInfo userInfo;
    private Role role;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        userInfo = new UserInfo();
        userInfo.setPhoneNumber("1234567890");
        //userInfo.setRole(new Role(RoleName.CUSTOMER));

        role = new Role();
        role.setRoleId(2L);
        role.setName(RoleName.CUSTOMER);

        wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0.0);
    }

    
    @Test
    void testRegister_UserAlreadyRegistered() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserAlreadyRegisteredException.class, () -> authService.register(user, userInfo));
    }


    @Test
    void testUpdatePassword_Success() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        authService.updatePassword(user.getEmail(), "newPassword");

        // Assert
        assertEquals("newPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.updatePassword(user.getEmail(), "newPassword"));
    }

    @Test
    void testGetWalletBalance_Success() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

        // Act
        Double balance = authService.getWalletBalance(user.getEmail());

        // Assert
        assertEquals(0.0, balance);
    }

    @Test
    void testGetWalletBalance_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.getWalletBalance(user.getEmail()));
    }

    @Test
    void testRechargeWallet_Success() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

        // Act
        authService.rechargeWallet(user.getEmail(), 100.0);

        // Assert
        assertEquals(100.0, wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testRechargeWallet_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.rechargeWallet(user.getEmail(), 100.0));
    }


    @Test
    void testDeductFromWallet_UserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.deductFromwallet(user.getUserId(), 50f));
    }
}
