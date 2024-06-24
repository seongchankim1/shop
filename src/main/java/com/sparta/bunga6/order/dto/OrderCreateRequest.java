package com.sparta.bunga6.order.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderCreateRequest {
	private List<OrderLineRequest> orderLines;
	private String address;
}
