package com.sparta.bunga6.order.controller;

import com.sparta.bunga6.base.dto.CommonResponse;
import com.sparta.bunga6.order.dto.AddressRequest;
import com.sparta.bunga6.order.dto.OrderCreateRequest;
import com.sparta.bunga6.order.dto.OrderResponse;
import com.sparta.bunga6.order.entity.Order;
import com.sparta.bunga6.order.service.OrderService;
import com.sparta.bunga6.security.UserDetailsImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.bunga6.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.bunga6.util.ControllerUtil.getResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	/**
	 * 주문 생성
	 */
	@PostMapping
	public ResponseEntity<CommonResponse<?>> createOrder(
		@Valid @RequestBody OrderCreateRequest requestDto,
		BindingResult bindingResult,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		if (bindingResult.hasErrors()) {
			return getFieldErrorResponseEntity(bindingResult, "주문 실패");
		}
		OrderResponse response  = new OrderResponse(orderService.createOrder(requestDto,userDetails.getUser()));

		return getResponseEntity(response, "주문 성공");
	}

	/**
	 * 주문 단건 조회
	 */
	@GetMapping("/{ordersId}")
	public ResponseEntity<CommonResponse<?>> getOrders(
		@PathVariable Long ordersId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		OrderResponse response  = new OrderResponse(orderService.getOrder(ordersId,userDetails.getUser()));

		return getResponseEntity(response, "주문 조회 성공");
	}

	/**
	 * 주문 전체 조회
	 */
	@GetMapping
	public ResponseEntity<CommonResponse<?>> getAllOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<Order> ordersList = orderService.getAllOrders(userDetails.getUser());
		List<OrderResponse> response = ordersList.stream()
			.map(OrderResponse::new)
			.collect(Collectors.toList());

		return getResponseEntity(response, "주문 조회 성공");
	}

	/**
	 * 주문 수정
	 */
	@PatchMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> updateOrders(
		@Valid @RequestBody AddressRequest requestDto,
		BindingResult bindingResult,
		@PathVariable Long orderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		if (bindingResult.hasErrors()) {
			return getFieldErrorResponseEntity(bindingResult, "주문 수정 실패");
		}
		OrderResponse response  = new OrderResponse(orderService.updateOrder(requestDto,orderId,userDetails.getUser()));

		return getResponseEntity(response, "주문 수정 성공");
	}

	/**
	 * 주문 삭제
	 */
	@DeleteMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> deleteOrders(
		@PathVariable Long orderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		OrderResponse response  = new OrderResponse(orderService.deleteOrder(orderId,userDetails.getUser()));

		return getResponseEntity(response, "주문 삭제 성공");
	}
}