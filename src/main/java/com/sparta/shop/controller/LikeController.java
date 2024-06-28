package com.sparta.shop.controller;

import static com.sparta.shop.util.ControllerUtil.getResponseEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.shop.base.dto.CommonResponse;
import com.sparta.shop.dto.OrderResponse;
import com.sparta.shop.dto.ProductLikeResponse;
import com.sparta.shop.dto.ReviewLikeResponse;
import com.sparta.shop.entity.Like;
import com.sparta.shop.entity.Order;
import com.sparta.shop.repository.LikeRepository;
import com.sparta.shop.security.UserDetailsImpl;
import com.sparta.shop.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

	private final LikeService likeService;
	private final LikeRepository likeRepository;

	@PostMapping("/product/{product_id}")
	public ResponseEntity<CommonResponse<?>> productLike(
		@PathVariable Long product_id,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		String liked = "";
		Like like = likeService.productLike(product_id, userDetails.getUser());
		ProductLikeResponse response = new ProductLikeResponse(like);
		if (likeRepository.existsById(like.getId())) {
			liked = "등록!";
		} else {
			liked = "취소!";
		}
		return getResponseEntity(response, "좋아요 " + liked);
	}

	@PostMapping("/review/{review_id}")
	public ResponseEntity<CommonResponse<?>> reviewLike(
		@PathVariable Long review_id,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		String liked = "";
		Like like = likeService.reviewLike(review_id, userDetails.getUser());
		ReviewLikeResponse response = new ReviewLikeResponse(like);
		if (likeRepository.existsById(like.getId())) {
			liked = "등록!";
		} else {
			liked = "취소!";
		}
		return getResponseEntity(response, "좋아요 " + liked);
	}

	@GetMapping("/product")
	public ResponseEntity<CommonResponse<?>> getAllProductLikes(
		@PageableDefault(
			sort = "createdAt",
			size = 5,
			direction = Sort.Direction.DESC
		) Pageable pageable,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Page<Like> page = likeService.findAllProductLikes(pageable, userDetails.getUser());
		Page<ProductLikeResponse> response = page.map(ProductLikeResponse::new);

		return getResponseEntity(response, "상품 좋아요 목록");
	}

	@GetMapping("/review")
	public ResponseEntity<CommonResponse<?>> getAllReviewLikes(
		@PageableDefault(
			sort = "createdAt",
			size = 5,
			direction = Sort.Direction.DESC
		) Pageable pageable,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Page<Like> page = likeService.findAllReviewLikes(pageable, userDetails.getUser());
		Page<ReviewLikeResponse> response = page.map(ReviewLikeResponse::new);

		return getResponseEntity(response, "상품 좋아요 목록");
	}

}
