package com.crimsonlogic.onlinebiddingsystem.service;

import java.util.List;

import com.crimsonlogic.onlinebiddingsystem.entity.Order;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;

public interface OrderService {
	Order createOrder(Order order);
    List<Order> getAllOrders();
    List<Order> getOrdersByBuyer(User buyer);
	List<Order> getOrdersByBuyerId(Long id);
	List<Order> getOrdersByDeliveryPerson(User deliveryPerson);
	Order getOrderById(Long orderId) throws ResourceNotFoundException;
	Order updateOrder(Order order);
}
