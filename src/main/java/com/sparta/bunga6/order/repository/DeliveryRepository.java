package com.sparta.bunga6.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bunga6.order.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
