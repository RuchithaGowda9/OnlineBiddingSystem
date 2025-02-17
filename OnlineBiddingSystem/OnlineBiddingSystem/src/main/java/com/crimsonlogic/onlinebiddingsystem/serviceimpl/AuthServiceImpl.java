package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.dto.UserDto;
import com.crimsonlogic.onlinebiddingsystem.dto.UserInfoDto;
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
import com.crimsonlogic.onlinebiddingsystem.service.AuthService;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ruchitha
 *
 */
@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final UserInfoRepository userInfoRepository;
	private final RoleRepository roleRepository;
	private final WalletRepository walletRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public AuthServiceImpl(UserRepository userRepository, UserInfoRepository userInfoRepository,
			RoleRepository roleRepository, WalletRepository walletRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.userInfoRepository = userInfoRepository;
		this.roleRepository = roleRepository;
		this.walletRepository = walletRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional
	@Override
	public void register(User user, UserInfo userInfo) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new UserAlreadyRegisteredException("A user with the same email already exists");
		}
		if (userInfoRepository.findByPhoneNumber(userInfo.getPhoneNumber()).isPresent()) {
			throw new UserAlreadyRegisteredException("A user with the same email already exists ");
		}
		userRepository.save(user);

		userInfo.setUser(user);
		if (userInfo.getRole() != null) {
			Role role = getRoleByName(userInfo.getRole().getName());
			userInfo.setRole(role);
		} else {
			throw new RuntimeException("Role must be provided in UserInfo");
		}

		userInfoRepository.save(userInfo);
		if ("CUSTOMER".equalsIgnoreCase(userInfo.getRole().getName().toString())) {
			Wallet wallet = new Wallet();
			wallet.setUser(user);
			wallet.setBalance(0.0);
			walletRepository.save(wallet);
		}
	}

	@Override
	public String login(UserDto userDto) {
		User user = userRepository.findByEmail(userDto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));
		if (userDto.getPassword().equals(user.getPassword())) {
			UserInfo userInfo = userInfoRepository.findByUser(user)
					.orElseThrow(() -> new RuntimeException("UserInfo not found"));
			return getDashboardUrlByRole(userInfo.getRole().getRoleId());
		} else {
			throw new RuntimeException("Invalid credentials");
		}
	}

	private String getDashboardUrlByRole(Long roleId) {
		switch (roleId.intValue()) {
		case 1:
			return "/admin/dashboard";
		case 2:
			return "/customer/dashboard";
		case 3:
			return "/delivery/dashboard";
		default:
			throw new RuntimeException("Role not recognized");
		}
	}

	@Override
	public UserInfoDto getUserInfoByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		System.out.println("Retrieved User: " + user);

		UserInfo userInfo = userInfoRepository.findByUser(user)
				.orElseThrow(() -> new RuntimeException("UserInfo not found"));

		UserDto userDto = modelMapper.map(user, UserDto.class);
		System.out.println("Mapped UserDto: " + userDto);

		return modelMapper.map(userInfo, UserInfoDto.class);
	}

	private Role getRoleByName(RoleName roleName) {
		return roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role not found for name: " + roleName));
	}

	@Override
	public boolean validatePassword(String email, String password) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (!optionalUser.isPresent()) {
			return false;
		}

		User user = optionalUser.get();
		return user.getPassword().equals(password);
	}

	@Override
	public void updatePassword(String email, String newPassword) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (!optionalUser.isPresent()) {
			throw new RuntimeException("User not found"); // Handle user not found case
		}

		User user = optionalUser.get();

		user.setPassword(newPassword);

		userRepository.save(user);
	}

	@Override
	public Double getWalletBalance(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		Wallet wallet = walletRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Wallet not found"));
		return wallet.getBalance();
	}

	@Override
	public void rechargeWallet(String email, Double amount) {
		if (amount <= 0 || amount > 100000) {
			throw new RuntimeException("Recharge amount must be between 1 and 100,000 rupees.");
		}

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		Wallet wallet = walletRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Wallet not found"));

		wallet.setBalance(wallet.getBalance() + amount);
		walletRepository.save(wallet);
	}

	@Override
	public void deductFromwallet(Long buyerId, Float bidAmount) {
		User user = userRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("User not found"));
		Wallet wallet = walletRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Wallet not found"));
		wallet.setBalance(wallet.getBalance() - bidAmount);
		walletRepository.save(wallet);
	}

}
