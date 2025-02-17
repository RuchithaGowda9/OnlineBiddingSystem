package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.onlinebiddingsystem.entity.Address;
import com.crimsonlogic.onlinebiddingsystem.repository.AddressRepository;
import com.crimsonlogic.onlinebiddingsystem.service.AddressService;
@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	AddressRepository addressRepository;

	@Override
	public Address addAddress(Address address) {
		return addressRepository.save(address);
	}

	@Override
	public List<Address> getAllAddressesByUserId(Long userId) {
        return addressRepository.findAll().stream()
                .filter(address -> address.getUser().getUserId().equals(userId))
                .toList();
    }
	
	

}
