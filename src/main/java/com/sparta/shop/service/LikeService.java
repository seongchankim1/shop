package com.sparta.shop.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.shop.entity.Like;
import com.sparta.shop.entity.Product;
import com.sparta.shop.entity.User;
import com.sparta.shop.repository.LikeRepository;
import com.sparta.shop.repository.ProductRepository;
import com.sparta.shop.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

	private final LikeRepository likeRepository;
	private final ProductRepository productRepository;

	@Transactional
	public Like productLike(Long product_id, User user) {
		Product product = productRepository.findById(product_id)
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		Like like = likeRepository.findByProductAndUser(product, user);

		if (like != null) {
			likeRepository.delete(like);
			return like;
		} else {
			Like newLike = new Like(user, product);
			likeRepository.save(newLike);
			return newLike;
		}
	}
}
