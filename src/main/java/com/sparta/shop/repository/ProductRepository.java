package com.sparta.shop.repository;

import com.sparta.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

	Product findByName(String name);

	void deleteByName(String name);
}
