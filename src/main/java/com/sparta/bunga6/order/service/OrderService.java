package com.sparta.bunga6.order.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.bunga6.jwt.JwtProvider;
import com.sparta.bunga6.order.dto.AddressRequest;
import com.sparta.bunga6.order.dto.OrderCreateRequest;
import com.sparta.bunga6.order.dto.OrderLineRequest;
import com.sparta.bunga6.order.dto.OrderResponse;
import com.sparta.bunga6.order.entity.Delivery;
import com.sparta.bunga6.order.entity.DeliveryStatus;
import com.sparta.bunga6.order.entity.Order;
import com.sparta.bunga6.order.entity.OrderLine;
import com.sparta.bunga6.order.entity.OrderStatus;
import com.sparta.bunga6.order.entity.Product;
import com.sparta.bunga6.security.UserDetailsImpl;
import com.sparta.bunga6.user.entity.User;
import com.sparta.bunga6.order.repository.DeliveryRepository;
import com.sparta.bunga6.order.repository.OrderLineRepository;
import com.sparta.bunga6.order.repository.OrderRepository;
import com.sparta.bunga6.order.repository.ProductRepository;
import com.sparta.bunga6.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	private final ProductRepository productRepository;
	private final DeliveryRepository deliveryRepository;
	private final OrderLineRepository orderLineRepository;
	private Long count;

	@Transactional
	public Order createOrder(OrderCreateRequest orderRequest, User user) {
		// Order 생성
		Order order = new Order(user);
		order.updateStatus(OrderStatus.ORDERED);

		// Delivery 생성
		Delivery delivery = new Delivery(user);
		delivery.updateStatus(DeliveryStatus.PREPARING);
		order.setDelivery(delivery);

		for (OrderLineRequest orderLineRequest : orderRequest.getOrderLines()) {
			// ID로 Product 검색
			Product product = productRepository.findById(orderLineRequest.getProductId())
				.orElseThrow(() -> new IllegalArgumentException("일치하는 상품이 없습니다."));

			// OrderLine 생성
			OrderLine orderLine = new OrderLine(order, product, orderLineRequest.getCount());

			// Order에 OrderLine 추가
			order.addOrderLine(orderLine);
		}
		// Order 저장
		orderRepository.save(order);

		return order;
	}

	public Order getOrder(Long orderId, User user) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
		if (!user.getId().equals(order.getUser().getId())) {
			throw new IllegalArgumentException("자신의 주문만 조회할 수 있습니다.");
		}
		return order;
	}

	public List<Order> getAllOrders(User user) {
		List<Order> ordersList = orderRepository.findByUser(user);
		return ordersList;
	}

	@Transactional
	public Order updateOrder(AddressRequest requestDto,Long orderId, User user) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("일치하는 주문이 없습니다."));
		if (!user.getId().equals(order.getUser().getId())) {
			throw new IllegalArgumentException("자신의 주문만 수정 가능합니다.");
		}

		Delivery delivery = order.getDelivery();
		delivery.updateAddress(requestDto);

		deliveryRepository.save(delivery);

		return order;
	}

	@Transactional
	public Order deleteOrder(Long orderId, User user) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("일치하는 주문이 없습니다."));
		if (!user.getId().equals(order.getUser().getId())) {
			throw new IllegalArgumentException("자신의 주문만 삭제 가능합니다.");
		}

		orderRepository.delete(order);

		return order;
	}
}

