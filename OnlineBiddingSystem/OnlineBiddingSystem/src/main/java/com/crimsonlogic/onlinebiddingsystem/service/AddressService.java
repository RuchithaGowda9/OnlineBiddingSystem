package com.crimsonlogic.onlinebiddingsystem.service;

import java.util.List;

import com.crimsonlogic.onlinebiddingsystem.entity.Address;

public interface AddressService {

	Address addAddress(Address address);

	List<Address> getAllAddressesByUserId(Long userId);

}
