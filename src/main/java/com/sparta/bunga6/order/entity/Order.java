package com.sparta.bunga6.order.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
