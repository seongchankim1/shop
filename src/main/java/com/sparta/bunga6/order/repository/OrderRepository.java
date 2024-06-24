package com.sparta.bunga6.order.repository;

import com.sparta.bunga6.order.entity.Order;
import com.sparta.bunga6.product.entity.Product;
import com.sparta.bunga6.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
