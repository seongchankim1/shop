package com.sparta.shop.dto;

import com.sparta.shop.entity.User;

import lombok.Data;

@Data
public class UserResponse {

	private Long userId;
	private String name;
	private String username;
	private int followCount;

	public UserResponse(User user) {
		this.userId = user.getId();
		this.name = user.getName();
		this.username = user.getUsername();
		this.followCount = user.getFollowCount();
	}
}
