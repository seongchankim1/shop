package com.sparta.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.shop.entity.Like;
import com.sparta.shop.entity.Product;
import com.sparta.shop.entity.User;

public interface LikeRepository extends JpaRepository<Like, Long> {

	Like findByProductAndUser(Product product, User user);
}
