package com.sparta.bunga6.order.repository;

import java.util.List;
import com.sparta.bunga6.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.bunga6.order.entity.Order;
import com.sparta.bunga6.user.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);

	boolean existsByProductAndUserAndStatus(Product product, User user, String delivered);
}
