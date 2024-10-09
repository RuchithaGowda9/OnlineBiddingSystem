package com.crimsonlogic.onlinebiddingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.onlinebiddingsystem.entity.Address;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.service.AddressService;
import com.crimsonlogic.onlinebiddingsystem.service.UserService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth/addresses")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private UserService userService;

	@PostMapping("/addaddress")
	public Address addAddress(@RequestBody Address address) {
		System.out.println("Received address: " + address);
		Long userId = address.getUser().getUserId();
		User user = userService.findById(userId);

		if (user == null) {
			throw new RuntimeException("User not found with ID: " + userId);
		}
		address.setUser(user);
		return addressService.addAddress(address);
	}

	@GetMapping("/user/{userId}")
	public List<Address> getUserAddresses(@PathVariable Long userId) {
		return addressService.getAllAddressesByUserId(userId);
	}

}
