package com.sparta.bunga6.order.dto;

import com.sparta.bunga6.orderline.OrderLine;

import lombok.Data;

@Data
public class OrderLineResponse {

	private Long orderLineId;
	private Long productId;
	private int count;
	private int orderPrice;

	public OrderLineResponse(OrderLine orderLine) {
		this.orderLineId = orderLine.getId();
		this.productId = orderLine.getProduct().getId();
		this.count = orderLine.getCount();
		this.orderPrice = orderLine.getOrderPrice();
	}

}