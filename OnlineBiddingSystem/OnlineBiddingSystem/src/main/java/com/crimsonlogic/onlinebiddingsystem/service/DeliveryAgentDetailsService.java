package com.crimsonlogic.onlinebiddingsystem.service;

import java.util.List;

import com.crimsonlogic.onlinebiddingsystem.entity.DeliveryAgentDetails;
import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;

public interface DeliveryAgentDetailsService {
	    List<UserInfo> findDeliveryPersonsByAgencyName(String agencyName);
	    List<DeliveryAgentDetails> findAll();
	    List<String> findAllDeliveryAgencies();
	    
	}


