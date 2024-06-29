package com.sparta.shop.repository;

import com.sparta.shop.entity.Follow;
import com.sparta.shop.entity.Review;
import com.sparta.shop.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductIdOrderByCreatedAt(Long productId);

	Review findByContent(String content);

	Page<Review> findAllByUserIn(Pageable pageable, List<User> users);

}