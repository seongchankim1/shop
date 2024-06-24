package com.sparta.bunga6.review.service;

import com.sparta.bunga6.product.entity.Product;
import com.sparta.bunga6.product.repository.ProductRepository;
import com.sparta.bunga6.review.dto.ReviewResponse;
import com.sparta.bunga6.review.entity.Review;
import com.sparta.bunga6.review.repository.ReviewRepository;
import com.sparta.bunga6.user.entity.User;
import com.sparta.bunga6.user.repository.UserRepository;
import com.sparta.bunga6.order.repository.OrderRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    // 리뷰 생성
    @Transactional
    public ReviewResponse createReview(Long productId, Long userId, String content) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("User with id " + userId + " not found")
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("Product with id " + productId + " not found")
        );

        if (!orderRepository.existsByProductAndUserAndStatus(product, user, "DELIVERED")) {
            throw new IllegalStateException("상품이 배송 완료되지 않았습니다.");
        }

        Review review = new Review(content, user, product);
        reviewRepository.save(review);
        return new ReviewResponse(review.getId(), review.getProduct().getId(), review.getUser().getId(), review.getContent());
    }

    // 전체 리뷰 조회
    public List<ReviewResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(review -> new ReviewResponse(review.getId(), review.getProduct().getId(), review.getUser().getId(), review.getContent()))
                .collect(Collectors.toList());
    }

    // 특정 상품의 리뷰 조회
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream()
                .map(review -> new ReviewResponse(review.getId(), review.getProduct().getId(), review.getUser().getId(), review.getContent()))
                .collect(Collectors.toList());
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponse updateReview(Long reviewId, String content, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("자신의 리뷰만 수정할 수 있습니다.");
        }

        review.setContent(content);
        return new ReviewResponse(review.getId(), review.getProduct().getId(), review.getUser().getId(), review.getContent());
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("자신의 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }
}