package com.crimsonlogic.onlinebiddingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crimsonlogic.onlinebiddingsystem.entity.Order;
import com.crimsonlogic.onlinebiddingsystem.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByBuyerId(User buyerId);

	List<Order> findByDeliveryPersonId(User deliveryPerson);
}

