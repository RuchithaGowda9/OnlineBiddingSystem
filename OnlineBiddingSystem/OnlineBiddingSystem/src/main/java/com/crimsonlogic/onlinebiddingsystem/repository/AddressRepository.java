package com.crimsonlogic.onlinebiddingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crimsonlogic.onlinebiddingsystem.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findByUserUserId(Long userId);
}
