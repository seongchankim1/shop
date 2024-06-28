package com.sparta.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.shop.dto.FollowRequest;
import com.sparta.shop.entity.Follow;
import com.sparta.shop.entity.User;
import com.sparta.shop.repository.FollowRepository;
import com.sparta.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	@Transactional
	public Follow follow(FollowRequest request, User following) {

		User follower = userRepository.findById(request.getFollowerId())
			.orElseThrow(() -> new RuntimeException("팔로우 대상을 찾을 수 없습니다."));

		Follow follow = followRepository.findByFollowerAndFollowing(follower, following);
		if (follow == null) {
			Follow newFollow = new Follow(follower, following);
			followRepository.save(newFollow);
			return newFollow;
		} else {
			throw new IllegalArgumentException("이미 팔로우중입니다.");
		}
	}

	@Transactional
	public Follow unFollow(FollowRequest request, User user) {

		User following = user;
		User follower = userRepository.findById(request.getFollowerId())
			.orElseThrow(() -> new RuntimeException("언팔로우 대상을 찾을 수 없습니다."));

		Follow follow = followRepository.findByFollowerAndFollowing(follower, following);
		if (follow != null) {
			followRepository.delete(follow);
			return follow;
		} else {
			throw new IllegalArgumentException("팔로우중이지 않습니다.");
		}
	}
}
