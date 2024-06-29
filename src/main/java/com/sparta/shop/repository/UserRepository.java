package com.sparta.shop.repository;

import com.sparta.shop.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

	Page<User> findAllByOrderByFollowCountDesc(Pageable pageable);


}
