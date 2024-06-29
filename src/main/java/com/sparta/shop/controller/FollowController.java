package com.sparta.shop.controller;

import static com.sparta.shop.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.shop.util.ControllerUtil.getResponseEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.shop.base.dto.CommonResponse;
import com.sparta.shop.dto.FollowRequest;
import com.sparta.shop.dto.FollowResponse;
import com.sparta.shop.dto.OrderRequest;
import com.sparta.shop.dto.OrderResponse;
import com.sparta.shop.dto.ReviewResponse;
import com.sparta.shop.entity.Follow;
import com.sparta.shop.entity.Order;
import com.sparta.shop.entity.Review;
import com.sparta.shop.entity.User;
import com.sparta.shop.security.UserDetailsImpl;
import com.sparta.shop.service.FollowService;
import com.sparta.shop.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

	private final FollowService followService;
	private final ReviewService reviewService;

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

	@GetMapping("/review/sort/time")
	public ResponseEntity<CommonResponse<?>> getFollowReviewByCreatedAt(
		@PageableDefault(
			sort = "createdAt",
			size = 5,
			direction = Sort.Direction.DESC
		) Pageable pageable,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Page<Review> page = reviewService.findAllReviewByFollowingList(pageable, userDetails.getUser());
		Page<ReviewResponse> response = page.map(ReviewResponse::new);

		return getResponseEntity(response, "팔로우한 사람들의 리뷰 조회 성공 (최신순)");
	}

	@GetMapping("/review/sort/name")
	public ResponseEntity<CommonResponse<?>> getFollowReviewByName(
		@PageableDefault(
			sort = "username",
			size = 5,
			direction = Sort.Direction.ASC
		) Pageable pageable,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Page<Review> page = reviewService.findAllReviewByFollowingList(pageable, userDetails.getUser());
		Page<ReviewResponse> response = page.map(ReviewResponse::new);

		return getResponseEntity(response, "팔로우한 사람들의 리뷰 조회 성공 (이름순)");
	}


}
