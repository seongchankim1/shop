package com.sparta.shop.dto;

import com.sparta.shop.orderline.OrderLine;

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