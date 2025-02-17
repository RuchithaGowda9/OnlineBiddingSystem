package com.crimsonlogic.onlinebiddingsystem.service;

import com.crimsonlogic.onlinebiddingsystem.dto.UserInfoDto;
import com.crimsonlogic.onlinebiddingsystem.entity.User;

public interface UserService {
	 public boolean updateUserInfo(UserInfoDto userInfoDto);
	 User findById(Long id);
	public User getUserById(Long userId);
}
