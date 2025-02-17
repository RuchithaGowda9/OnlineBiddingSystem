package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.onlinebiddingsystem.dto.UserInfoDto;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import com.crimsonlogic.onlinebiddingsystem.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId).get();
	}
	@Override
	public boolean updateUserInfo(UserInfoDto userInfoDto) {
		return false;
	}

}
