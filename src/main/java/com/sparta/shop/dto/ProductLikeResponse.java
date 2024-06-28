package com.sparta.shop.dto;

import com.sparta.shop.entity.Like;

import lombok.Data;

@Data
public class ProductLikeResponse {

	private Long id;
	private String username;
	private String productName;

	public ProductLikeResponse(Like like) {
		this.id = like.getId();
		this.username = like.getUser().getUsername();
		this.productName = like.getProduct().getName();
	}
}
