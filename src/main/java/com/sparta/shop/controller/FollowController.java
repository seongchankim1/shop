package com.sparta.shop.controller;

import static com.sparta.shop.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.shop.util.ControllerUtil.getResponseEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.shop.base.dto.CommonResponse;
import com.sparta.shop.dto.FollowRequest;
import com.sparta.shop.dto.FollowResponse;
import com.sparta.shop.dto.OrderRequest;
import com.sparta.shop.dto.OrderResponse;
import com.sparta.shop.entity.Follow;
import com.sparta.shop.entity.Order;
import com.sparta.shop.security.UserDetailsImpl;
import com.sparta.shop.service.FollowService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

	private final FollowService followService;

	@PostMapping
	public ResponseEntity<CommonResponse<?>> follow(
		@Valid @RequestBody FollowRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return getFieldErrorResponseEntity(bindingResult, "팔로우 실패");
		}
		Follow follow = followService.follow(request, userDetails.getUser());
		FollowResponse response  = new FollowResponse(follow);

		return getResponseEntity(response, "팔로우 성공");
	}

	@DeleteMapping
	public ResponseEntity<CommonResponse<?>> unFollow(
		@Valid @RequestBody FollowRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return getFieldErrorResponseEntity(bindingResult, "팔로우 취소 실패");
		}
		Follow follow = followService.unFollow(request, userDetails.getUser());
		FollowResponse response  = new FollowResponse(follow);

		return getResponseEntity(response, "팔로우 취소 성공");
	}

}
