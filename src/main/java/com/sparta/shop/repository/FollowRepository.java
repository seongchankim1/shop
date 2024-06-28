package com.sparta.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.shop.entity.Follow;
import com.sparta.shop.entity.User;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	Follow findByFollowerAndFollowing(User follower, User following);
}
