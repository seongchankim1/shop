package com.sparta.shop.entity;

import com.sparta.shop.base.entity.Timestamped;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries")
public class Delivery extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private Long id;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private DeliveryStatus status; // 배송 상태 [PREPARING, SHIPPING, DELIVERED]

	public Delivery(String address) {
		this.address = address;
	}

	public void updateAddress(String address) {
		this.address = address;
	}

}
