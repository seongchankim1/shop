package com.sparta.bunga6.delivery;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.order.dto.AddressRequest;
import com.sparta.bunga6.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	@Enumerated(value = EnumType.STRING)
	private DeliveryStatus status;

	@Column(nullable = false)
	private String address;

	public Delivery(User user) {
		this.address = user.getAddress();
	}

	public void updateStatus(DeliveryStatus status) {
		this.status = status;
	}

	public void updateAddress(AddressRequest request) {
		this.address = request.getAddress();
	}
}
