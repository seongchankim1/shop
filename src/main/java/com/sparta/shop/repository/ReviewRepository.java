package com.sparta.shop.repository;

import com.sparta.shop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductIdOrderByCreatedAt(Long productId);

}