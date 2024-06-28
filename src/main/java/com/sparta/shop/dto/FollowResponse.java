package com.sparta.shop.dto;

import com.sparta.shop.entity.Follow;

import lombok.Data;
import lombok.Getter;

@Data
public class FollowResponse {

	private Long id;
	private String follower;
	private String following;

	public FollowResponse(Follow follow) {
		this.id = follow.getId();
		this.follower = follow.getFollower().getUsername();
		this.following = follow.getFollowing().getUsername();
	}
}
