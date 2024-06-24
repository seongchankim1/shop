package com.sparta.bunga6.review.controller;

import com.sparta.bunga6.review.dto.ReviewResponse;
import com.sparta.bunga6.review.service.ReviewService;
import com.sparta.bunga6.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping // 리뷰 생성
    public ResponseEntity<ReviewResponse> createReview(
            @RequestParam Long productId,
            @RequestParam String content,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReviewResponse createdReview = reviewService.createReview(productId, userDetails.getUser().getId(), content);
        return new ResponseEntity<>(createdReview, HttpStatus.OK);
    }

    @GetMapping // 전체 리뷰 조회
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}") // 특정 상품의 리뷰 조회
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}") // 리뷰 수정
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @RequestParam String content,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReviewResponse updatedReview = reviewService.updateReview(reviewId, content, userDetails.getUser().getId());
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}") // 리뷰 삭제
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reviewService.deleteReview(reviewId, userDetails.getUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}