package com.sparta.bunga6.order.entity;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.delivery.Delivery;
import com.sparta.bunga6.orderline.OrderLine;
import com.sparta.bunga6.user.entity.User;
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
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLine> orderLineList = new ArrayList<>();

	@Column
	private int totalPrice;

	public Order(User user) {
		this.user = user;
	}

	public void updateStatus(OrderStatus status) {
		this.status = status;
	}

	public void addOrderLine(OrderLine orderLine) {
		orderLineList.add(orderLine);
		orderLine.assignOrder(this);
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;

	}
}
