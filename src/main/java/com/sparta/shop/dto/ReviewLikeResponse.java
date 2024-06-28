package com.sparta.shop.dto;

import com.sparta.shop.entity.Like;

import lombok.Data;

@Data
public class ReviewLikeResponse {

	private Long id;
	private String username;
	private Long reviewId;
	private String reviewContent;

	public ReviewLikeResponse(Like like) {
		this.id = like.getId();
		this.username = like.getUser().getUsername();
		this.reviewId = like.getReview().getId();
		this.reviewContent = like.getReview().getContent();
	}
}
