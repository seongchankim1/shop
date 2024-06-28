package com.sparta.shop.entity;

import com.sparta.shop.base.entity.Timestamped;
import com.sparta.shop.orderline.OrderLine;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private OrderStatus status; // 주문 상태 [ORDERED, CANCELED]

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<OrderLine> orderLines = new ArrayList<>();

	/**
	 * 정적 팩토리 메서드
	 */
	public static Order of(User user, Delivery delivery, OrderLine... orderLines) {
		Order order = new Order();
		order.user = user;
		order.delivery = delivery;
		for (OrderLine orderLine : orderLines) {
			order.addOrderItem(orderLine);
		}
		order.status = OrderStatus.ORDERED;

		return order;
	}

    /**
	 * 연관관계 편의 메서드
	 */
	public void addOrderItem(OrderLine orderLine) {
		orderLines.add(orderLine);
		orderLine.setOrder(this);
	}

	/**
	 * 주문 취소
	 */
	public void cancel() {
		checkDeliveryStatus();
		this.status = OrderStatus.CANCELED;
		for (OrderLine orderLine : orderLines) {
			orderLine.cancel();
		}
	}

	/**
	 * 배송 주소 변경
	 */
	public void updateAddress(String address) {
		checkDeliveryStatus();
		this.delivery.updateAddress(address);
	}

	// 배송 상태 확인
	private void checkDeliveryStatus() {
		if (delivery.getStatus() != DeliveryStatus.PREPARING) {
			throw new IllegalStateException("이미 배송이 시작된 주문입니다.");
		}
	}

	/**
	 * 전체 주문 가격 조회
	 */
	public int getTotalPrice() {
		int totalPrice = 0;
		for (OrderLine orderLine : orderLines) {
			totalPrice += orderLine.getTotalPrice();
		}
		return totalPrice;
	}

	public void verifyUser(User user) {
		if (!user.getId().equals(this.getUser().getId())) {
			throw new IllegalStateException("해당 주문의 주문자가 아닙니다.");
		}
	}

}