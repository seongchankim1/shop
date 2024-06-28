package com.sparta.shop.controller;

import static com.sparta.shop.util.ControllerUtil.getResponseEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.shop.base.dto.CommonResponse;
import com.sparta.shop.dto.LikeResponse;
import com.sparta.shop.entity.Like;
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

	@PostMapping("/{product_id}")
	public ResponseEntity<CommonResponse<?>> productLike(
		@PathVariable Long product_id,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		String liked = "";
		Like like = likeService.productLike(product_id, userDetails.getUser());
		LikeResponse response = new LikeResponse(like);
		if (likeRepository.existsById(like.getId())) {
			liked = "등록!";
		} else {
			liked = "취소!";
		}
		return getResponseEntity(response, "좋아요 " + liked);
	}

}
