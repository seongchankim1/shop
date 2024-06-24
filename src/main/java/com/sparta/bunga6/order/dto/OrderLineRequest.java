package com.sparta.bunga6.order.dto;

import lombok.Data;

@Data
public class OrderLineRequest {
	private Long productId;
	private int count;
}
