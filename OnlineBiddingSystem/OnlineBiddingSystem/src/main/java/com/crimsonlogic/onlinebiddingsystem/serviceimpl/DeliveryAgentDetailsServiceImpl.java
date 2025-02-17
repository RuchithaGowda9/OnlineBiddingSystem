package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.onlinebiddingsystem.dto.DeliveryAgentDTO;
import com.crimsonlogic.onlinebiddingsystem.entity.DeliveryAgentDetails;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;
import com.crimsonlogic.onlinebiddingsystem.repository.DeliveryAgentDetailsRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserInfoRepository;
import com.crimsonlogic.onlinebiddingsystem.service.DeliveryAgentDetailsService;

@Service
public class DeliveryAgentDetailsServiceImpl implements DeliveryAgentDetailsService {

    @Autowired
    private DeliveryAgentDetailsRepository deliveryAgentRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public List<UserInfo> findDeliveryPersonsByAgencyName(String agencyName) {
        List<DeliveryAgentDetails> deliveryAgents = deliveryAgentRepository.findByDeliveryAgencyName(agencyName);
        List<UserInfo> deliveryPersons = new ArrayList<>();
        
        for (DeliveryAgentDetails agent : deliveryAgents) {
            User user = agent.getUser(); 

            Optional<UserInfo> optionalUserInfo = userInfoRepository.findByUser(user);
            
            optionalUserInfo.ifPresent(deliveryPersons::add);
        }
        return deliveryPersons;
    }
    
    @Override
    public List<String> findAllDeliveryAgencies() {
        return deliveryAgentRepository.findAll()
                .stream()
                .map(DeliveryAgentDetails::getDeliveryAgencyName) 
                .distinct() 
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryAgentDetails> findAll() {
        return deliveryAgentRepository.findAll();
    }
    
    
    
}


