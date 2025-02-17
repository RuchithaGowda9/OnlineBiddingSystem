package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.entity.UserInfo;
import com.crimsonlogic.onlinebiddingsystem.service.DeliveryAgentDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class DeliveryAgentDetailsController {

	@Autowired
    private DeliveryAgentDetailsService deliveryAgentService;

    @GetMapping("/delivery-agents")
    public ResponseEntity<List<UserInfo>> getDeliveryPersons(@RequestParam String agencyName) {
    	System.out.println(agencyName);
        List<UserInfo> deliveryPersons = deliveryAgentService.findDeliveryPersonsByAgencyName(agencyName);
        System.out.println(deliveryPersons);
        return ResponseEntity.ok(deliveryPersons);
    }
    
    @GetMapping("/agencies")
    public ResponseEntity<List<String>> getAllDeliveryAgencies() {
        List<String> agencies = deliveryAgentService.findAllDeliveryAgencies();
        return ResponseEntity.ok(agencies);
    }
    
    
    
}
