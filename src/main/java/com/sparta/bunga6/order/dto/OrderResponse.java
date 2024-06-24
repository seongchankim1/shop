package com.sparta.bunga6.order.dto;

import com.sparta.bunga6.delivery.Delivery;
import com.sparta.bunga6.delivery.DeliveryStatus;
import com.sparta.bunga6.order.entity.Order;
import com.sparta.bunga6.order.entity.OrderStatus;
import com.sparta.bunga6.orderline.OrderLine;
import com.sparta.bunga6.user.entity.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponse {

	private Long orderId;
	private Long userId;
	private Long deliveryId;
	private OrderStatus orderStatus;
	private DeliveryStatus deliveryStatus;
	private String address;
	private int totalPrice;
	private List<OrderLineResponse> orderLines;

	public OrderResponse(Order order, User user, Delivery delivery, OrderLine orderLine) {
		this.orderId = order.getId();
		this.userId = user.getId();
		this.deliveryId = delivery.getId();
		this.orderStatus = order.getStatus();
		this.deliveryStatus = delivery.getStatus();
		this.address = delivery.getAddress();
	}

	public OrderResponse(Order order) {
		this.orderId = order.getId();
		this.userId = order.getUser().getId();
		this.orderLines = order.getOrderLines().stream()
			.map(OrderLineResponse::new)
			.collect(Collectors.toList());
		this.deliveryId = order.getDelivery().getId();
		this.orderStatus = order.getStatus();
		this.deliveryStatus = order.getDelivery().getStatus();
		this.address = order.getDelivery().getAddress();
		this.totalPrice = order.getOrderLines().stream()
			.mapToInt(OrderLine::getOrderPrice)
			.sum();
	}
}
