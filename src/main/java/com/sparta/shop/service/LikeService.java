package com.sparta.shop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.shop.entity.Like;
import com.sparta.shop.entity.Product;
import com.sparta.shop.entity.Review;
import com.sparta.shop.entity.User;
import com.sparta.shop.repository.LikeRepository;
import com.sparta.shop.repository.ProductRepository;
import com.sparta.shop.repository.ReviewRepository;
import com.sparta.shop.repository.UserRepository;
import com.sparta.shop.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

	private final LikeRepository likeRepository;
	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;

	@Transactional
	public Like productLike(Long product_id, User user) {
		Product product = productRepository.findById(product_id)
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		Like like = likeRepository.findByProductAndUser(product, user);
		int likeCount = user.getProductLikeCount();
		if (like != null) {
			likeRepository.delete(like);
			user.updateProductLikeCount(likeCount - 1);
			userRepository.save(user);
			return like;
		} else {
			Like newLike = new Like(user, product);
			likeRepository.save(newLike);
			validateLike(newLike.getProduct(), newLike.getReview());
			user.updateProductLikeCount(likeCount + 1);
			userRepository.save(user);
			return newLike;
		}
	}

	@Transactional
	public Like reviewLike(Long review_id, User user) {
		Review review = reviewRepository.findById(review_id)
			.orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

		Like like = likeRepository.findByReviewAndUser(review, user);
		int reviewCount = user.getReviewLikeCount();

		if (like != null) {
			likeRepository.delete(like);
			user.updateReviewLikeCount(reviewCount - 1);
			userRepository.save(user);
			return like;
		} else {
			Like newLike = new Like(user, review);
			likeRepository.save(newLike);
			validateLike(newLike.getProduct(), newLike.getReview());
			user.updateReviewLikeCount(reviewCount + 1);
			userRepository.save(user);
			return newLike;
		}
	}


	// 혹시 모를 중복 방지
	private void validateLike(Product product, Review review) {
		if (product != null && review != null) {
			throw new IllegalArgumentException("리뷰와 상품에 동시에 좋아요 할 수 없습니다.");
		}
	}

	public Page<Like> findAllProductLikes(Pageable pageable, User user) {

		return likeRepository.findByUserAndProductIsNotNull(user, pageable);
			}

	public Page<Like> findAllReviewLikes(Pageable pageable, User user) {

		return likeRepository.findByUserAndReviewIsNotNull(user, pageable);
	}
}
