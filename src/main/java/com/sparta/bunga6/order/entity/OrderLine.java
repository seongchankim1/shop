package com.sparta.bunga6.order.entity;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.order.dto.OrderCreateRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_lines")
public class OrderLine extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_line_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private int count;

	@Column
	private int orderPrice;

	public OrderLine(Order order, Product product, int count) {
		this.count = count;
		this.order = order;
		this.product = product;
		this.orderPrice = product.getPrice() * count;
	}

	public void assignOrder(Order order) {
		this.order = order;
	}
}
