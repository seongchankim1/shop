package com.sparta.bunga6.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bunga6.delivery.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
