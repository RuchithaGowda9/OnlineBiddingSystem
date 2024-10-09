package com.crimsonlogic.onlinebiddingsystem.dto;

import lombok.Data;

/**
 * @author Ruchitha
 *
 */
@Data
	public class OrderDto {
	    private Long orderId;
	    private String productName;
	    private Long buyerId;
	    private Long sellerId;
	    private Long deliveryPersonId;
	    private String orderStatus;
	    private Float bidAmount;

}
