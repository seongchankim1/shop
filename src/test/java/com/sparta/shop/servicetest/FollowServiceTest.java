package com.sparta.shop.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.sparta.shop.dto.FollowRequest;
import com.sparta.shop.dto.SignupRequest;
import com.sparta.shop.entity.Follow;
import com.sparta.shop.entity.User;
import com.sparta.shop.entity.UserRole;
import com.sparta.shop.jwt.JwtProvider;
import com.sparta.shop.repository.FollowRepository;
import com.sparta.shop.repository.UserRepository;
import com.sparta.shop.service.FollowService;

@SpringBootTest
class FollowServiceTest {

	@Autowired
	FollowService followService;
	@Autowired
	private JwtProvider jwtUtil;
	private MockHttpServletResponse response;
	private String token;
	private User user;
	private User follower;
	private User following;
	@Autowired
	private FollowRepository followRepository;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setUsername("following");
		signupRequest.setPassword("Test123!");
		signupRequest.setName("팔로잉");
		signupRequest.setAddress("우리집");

		// Create a new User entity
		following = new User(signupRequest, "encodedPassword", UserRole.USER);

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setUsername("follower");
		signupRequest2.setPassword("Test123!");
		signupRequest2.setName("팔로워");
		signupRequest2.setAddress("너네집");

		// Create a new User entity
		follower = new User(signupRequest2, "encodedPassword", UserRole.USER);

		response = new MockHttpServletResponse();
		token = jwtUtil.createAccessToken(following.getUsername(), UserRole.USER);
		response.addHeader("Authorization", token);

		userRepository.save(follower);
		userRepository.save(following);

	}

	@AfterEach
	void tearDown() {
		userRepository.deleteById(follower.getId());
		userRepository.deleteById(following.getId());
	}

	@Test
	@DisplayName("팔로우 테스트")
	void Test1() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", token);

		FollowRequest followRequest = new FollowRequest();
		followRequest.setFollowerId(follower.getId());
		Follow follow = followService.follow(followRequest, following);

		assertEquals(follow.getFollower().getUsername(), follower.getUsername());
		assertEquals(follow.getFollowing().getUsername(), following.getUsername());
	}

	@Test
	@DisplayName("팔로우 취소 테스트")
	void Test2() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", token);

		FollowRequest followRequest = new FollowRequest();
		followRequest.setFollowerId(follower.getId());
		Follow follow = followService.follow(followRequest, following);
		follow = followService.unFollow(followRequest, following);

		assertNotEquals(follow.getId(), followRepository.findById(follow.getId()));
	}
}