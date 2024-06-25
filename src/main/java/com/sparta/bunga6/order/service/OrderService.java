package com.sparta.bunga6.order.service;

import com.sparta.bunga6.delivery.Delivery;
import com.sparta.bunga6.order.dto.OrderAddrRequest;
import com.sparta.bunga6.order.dto.OrderRequest;
import com.sparta.bunga6.order.entity.Order;
import com.sparta.bunga6.order.repository.OrderRepository;
import com.sparta.bunga6.orderline.OrderLine;
import com.sparta.bunga6.product.entity.Product;
import com.sparta.bunga6.product.service.ProductService;
import com.sparta.bunga6.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

	/**
	 * 주문 생성
	 */
	@Transactional
	public Order createOrder(OrderRequest request, User user) {
		Product product = productService.findProduct(request.getProductId());
		OrderLine orderLine = OrderLine.of(product, product.getPrice(), request.getCount());
		Delivery delivery = new Delivery(user.getAddress());

		Order order = Order.of(user, delivery, orderLine);

		orderRepository.save(order);

		return order;
	}

	/**
	 * 전체 주문 조회 (관리자 전용)
	 */
	public Page<Order> findAllOrders(Pageable pageable, User admin) {
		admin.validateAdmin();
		return orderRepository.findAll(pageable);
	}

    /**
     * 주문 조회
     */
    public Order findOrder(Long orderId, User user) {
		Order order = orderRepository.findById(orderId).orElseThrow(() ->
				new NullPointerException("해당 주문이 존재하지 않습니다."));
		order.verifyUser(user);

		return order;
    }

	/**
     * 배송 주소 변경
     */
    @Transactional
    public Order updateAddress(OrderAddrRequest request, User user) {
        Order order = findOrder(request.getOrderId(), user);
        order.updateAddress(request.getAddress());

        return order;
    }

    /**
     * 주문 취소
     */
    @Transactional
    public Long cancelOrder(Long orderId, User user) {
        Order order = findOrder(orderId, user);
        order.cancel();

        return orderId;
    }

}