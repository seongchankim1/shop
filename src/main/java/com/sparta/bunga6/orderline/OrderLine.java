package com.sparta.bunga6.orderline;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.order.entity.Order;
import com.sparta.bunga6.product.entity.Product;
import jakarta.persistence.*;
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

	@Setter
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private int orderPrice; // 주문 가격

	@Column(nullable = false)
	private int count; // 주문 수량

	/**
	 * 정적 팩토리 메서드
	 */
	public static OrderLine of(Product product, int orderPrice, int count) {
		OrderLine orderLine = new OrderLine();
		orderLine.product = product;
		orderLine.orderPrice = orderPrice;
		orderLine.count = count;

		product.removeStock(count);

		return orderLine;
	}

	/**
	 * 주문 취소
	 */
	public void cancel() {
		getProduct().addStock(count);
	}

	/**
	 * 주문라인 전체 가격 조회
	 */
	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}

}