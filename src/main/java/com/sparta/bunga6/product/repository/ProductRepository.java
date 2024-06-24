package com.sparta.bunga6.product.repository;

import com.sparta.bunga6.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

}
