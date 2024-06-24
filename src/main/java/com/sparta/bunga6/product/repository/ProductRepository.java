package com.sparta.bunga6.product.repository;

import com.sparta.bunga6.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllBy(Pageable pageable);
    boolean existsByname(String name);

}
