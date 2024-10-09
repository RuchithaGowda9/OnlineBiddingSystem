package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.onlinebiddingsystem.entity.Order;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.onlinebiddingsystem.repository.OrderRepository;
import com.crimsonlogic.onlinebiddingsystem.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Order createOrder(Order order) {
		return orderRepository.save(order);
	}
	@Override
    public List<Order> getOrdersByBuyer(User buyer) {
        return orderRepository.findByBuyerId(buyer);
    }

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	@Override
    public List<Order> getOrdersByBuyerId(Long id) {
        User buyer = new User();
        buyer.setUserId(id);
        return orderRepository.findByBuyerId(buyer);
    }
	@Override
    public List<Order> getOrdersByDeliveryPerson(User deliveryPerson) {
        return orderRepository.findByDeliveryPersonId(deliveryPerson);
    }
	@Override
	public Order getOrderById(Long orderId) throws ResourceNotFoundException {
	    return orderRepository.findById(orderId)
	            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
	}
	@Override
	public Order updateOrder(Order order) {
	    return orderRepository.save(order);
	}
}
