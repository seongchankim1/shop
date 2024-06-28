package com.sparta.shop.service;

import com.sparta.shop.entity.Product;
import com.sparta.shop.dto.ReviewRequest;
import com.sparta.shop.entity.Review;
import com.sparta.shop.repository.ReviewRepository;
import com.sparta.shop.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;

    /**
     * 리뷰 작성
     */
    @Transactional
    public Review createReview(ReviewRequest request, User user) {
        Product product = productService.findProduct(request.getProductId());
        Review review = Review.of(request.getContent(), product, user);

        return reviewRepository.save(review);
    }

    /**
     * 특정 게시물의 전체 댓글 조회
     */
    public List<Review> findAllProductReview(Long productId) {
        return reviewRepository.findAllByProductIdOrderByCreatedAt(productId);
    }

    /**
     * 특정 댓글 조회
     */
    public Review findReview(Long productId, Long reviewId) {
        Review review = findById(reviewId);
        review.verifyProduct(productId);

        return review;
    }

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Review updateReview(Long productId, Long reviewId, String content, User user) {
        Review review = findReview(productId, reviewId);
        review.verifyUser(user.getId());
        review.update(content);

        return review;
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public Long deleteReview(Long productId, Long reviewId, User user) {
        Review review = findReview(productId, reviewId);
        review.verifyUser(user.getId());
        reviewRepository.delete(review);

        return reviewId;
    }

}