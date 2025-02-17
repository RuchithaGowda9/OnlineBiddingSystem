package com.crimsonlogic.onlinebiddingsystem.service;

import com.crimsonlogic.onlinebiddingsystem.dto.UserDto;
import com.crimsonlogic.onlinebiddingsystem.dto.UserInfoDto;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;

public interface AuthService {
    void register(User user, UserInfo userInfo);
    String login(UserDto userDto);
	UserInfoDto getUserInfoByEmail(String email);
	boolean validatePassword(String email, String password);
	void updatePassword(String email, String newPassword);
	Double getWalletBalance(String email);
	void rechargeWallet(String email, Double amount);
	void deductFromwallet(Long buyerId, Float bidAmount);
}
