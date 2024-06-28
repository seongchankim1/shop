package com.sparta.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.shop.entity.Like;
import com.sparta.shop.entity.Product;
import com.sparta.shop.entity.Review;
import com.sparta.shop.entity.User;

public interface LikeRepository extends JpaRepository<Like, Long> {

	Like findByProductAndUser(Product product, User user);

	Like findByReviewAndUser(Review review, User user);

	Page<Like> findByUserAndProductIsNotNull(User user, Pageable pageable);
}
