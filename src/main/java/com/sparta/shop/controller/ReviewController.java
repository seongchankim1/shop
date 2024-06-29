package com.sparta.shop.controller;

import com.sparta.shop.base.dto.CommonResponse;
import com.sparta.shop.dto.ReviewRequest;
import com.sparta.shop.dto.ReviewResponse;
import com.sparta.shop.entity.Review;
import com.sparta.shop.service.ReviewService;
import com.sparta.shop.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.shop.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/{productId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createReview(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "리뷰 작성 실패");
        }
        validatePathIdWithBody(productId, request.getProductId());

        Review review = reviewService.createReview(request, userDetails.getUser());
        ReviewResponse response = new ReviewResponse(review);

        return getResponseEntity(response, "리뷰 작성 성공");
    }

    /**
     * 특정 상품의 전체 리뷰 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> findAllProductReview(
            @PathVariable Long productId
    ) {
        List<ReviewResponse> response = reviewService.findAllProductReview(productId).stream()
                .map(ReviewResponse::new).toList();

        return getResponseEntity(response, "상품 전체 리뷰 조회 성공");
    }

    /**
     * 리뷰 조회
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<?>> findReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId
    ) {
        Review review = reviewService.findReview(productId, reviewId);
        ReviewResponse response = new ReviewResponse(review);

        return getResponseEntity(response, "리뷰 조회 성공");
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<?>> updateReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "리뷰 수정 실패");
        }
        validatePathIdWithBody(productId, request.getProductId());

        Review review = reviewService.updateReview(productId, reviewId, request.getContent(), userDetails.getUser());
        ReviewResponse response = new ReviewResponse(review);

        return getResponseEntity(response, "리뷰 수정 성공");
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<?>> deleteReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = reviewService.deleteReview(productId, reviewId, userDetails.getUser());

        return getResponseEntity(response, "리뷰 삭제 성공");
    }
}