package com.crimsonlogic.onlinebiddingsystem.service;

import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;

public interface UserInfoService {

	UserInfo getUserInfoByUserId(Long userId);
	
}
