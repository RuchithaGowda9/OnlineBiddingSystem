package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.dto.ChangePasswordDto;
import com.crimsonlogic.onlinebiddingsystem.dto.RegistrationDto;
import com.crimsonlogic.onlinebiddingsystem.dto.UserDto;
import com.crimsonlogic.onlinebiddingsystem.dto.UserInfoDto;
import com.crimsonlogic.onlinebiddingsystem.entity.Role;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;
import com.crimsonlogic.onlinebiddingsystem.repository.RoleRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import com.crimsonlogic.onlinebiddingsystem.service.AuthService;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

/**
 * @author Ruchitha
 *
 */
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
    private final AuthService authService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationDto> register(@RequestBody RegistrationDto registrationDto, HttpSession session) {

        User userPayload = modelMapper.map(registrationDto.getUser(), User.class);
        UserInfo userInfoPayload = modelMapper.map(registrationDto.getUserInfo(), UserInfo.class);

        if (registrationDto.getUserInfo().getRoleName() != null) {
            Role role = roleRepository.findByName(registrationDto.getUserInfo().getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + registrationDto.getUserInfo().getRoleName()));
            userInfoPayload.setRole(role);
        } else {
            throw new RuntimeException("Role name must be provided");
        }
        authService.register(userPayload, userInfoPayload);
        UserDto userResponse = modelMapper.map(userPayload, UserDto.class);
        UserInfoDto userInfoResponse = modelMapper.map(userInfoPayload, UserInfoDto.class);
        session.setAttribute("user", userResponse);
        session.setAttribute("userInfo", userInfoResponse);
        RegistrationDto responseDto = new RegistrationDto();
        responseDto.setUser(userResponse);
        responseDto.setUserInfo(userInfoResponse);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto, HttpSession session) {
       
        String dashboardUrl = authService.login(userDto);
        UserInfoDto userInfo = authService.getUserInfoByEmail(userDto.getEmail());
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto fullUserDto = modelMapper.map(user, UserDto.class);
        session.setAttribute("user", fullUserDto);
        session.setAttribute("userInfo", userInfo);
        
        System.out.println("User from session: " + fullUserDto);
        System.out.println("UserInfo from session: " + userInfo);

        return new ResponseEntity<>(dashboardUrl, HttpStatus.OK);
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpSession session) {
        UserInfoDto userInfo = (UserInfoDto) session.getAttribute("userInfo");
        UserDto user = (UserDto) session.getAttribute("user"); 
        System.out.println("User from session: " + user);
        System.out.println(userInfo);
        
        if (userInfo == null || user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("userInfo", userInfo);
        response.put("user", user); 
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!authService.validatePassword(user.getEmail(), changePasswordDto.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current password is incorrect");
        }

        authService.updatePassword(user.getEmail(), changePasswordDto.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
    @GetMapping("/wallet/balance")
    public ResponseEntity<Double> getWalletBalance(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Double balance = authService.getWalletBalance(user.getEmail());
        return ResponseEntity.ok(balance);
    }
    @PostMapping("/wallet/recharge")
    public ResponseEntity<String> rechargeWallet(@RequestBody Map<String, Double> requestBody, HttpSession session) {
        Double amount = requestBody.get("amount"); 
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            authService.rechargeWallet(user.getEmail(), amount);
            return ResponseEntity.ok("Wallet recharged successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); 
        return ResponseEntity.ok("Logged out successfully");
    }


}
