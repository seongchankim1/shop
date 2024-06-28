package com.sparta.shop.controller;

import com.sparta.shop.base.dto.CommonResponse;
import com.sparta.shop.dto.OrderAddrRequest;
import com.sparta.shop.dto.OrderRequest;
import com.sparta.shop.dto.OrderResponse;
import com.sparta.shop.entity.Order;
import com.sparta.shop.service.OrderService;
import com.sparta.shop.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.shop.util.ControllerUtil.*;

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
		@Valid @RequestBody OrderRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return getFieldErrorResponseEntity(bindingResult, "주문 실패");
		}
		Order order = orderService.createOrder(request, userDetails.getUser());
		OrderResponse response  = new OrderResponse(order);

		return getResponseEntity(response, "주문 성공");
	}

	/**
	 * 전체 주문 조회 (관리자 전용)
	 */
	@GetMapping
	public ResponseEntity<CommonResponse<?>> getAllOrders(
			@PageableDefault(
					sort = "createdAt",
					size = 5,
					direction = Sort.Direction.DESC
			) Pageable pageable,
			@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Page<Order> page = orderService.findAllOrders(pageable, userDetails.getUser());
		Page<OrderResponse> response = page.map(OrderResponse::new);

		return getResponseEntity(response, "전체 주문 조회 성공");
	}

	/**
	 * 주문 조회
	 */
	@GetMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> getOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Order order = orderService.findOrder(orderId, userDetails.getUser());
		OrderResponse response  = new OrderResponse(order);

		return getResponseEntity(response, "주문 조회 성공");
	}

	/**
	 * 배송 주소 변경
	 */
	@PatchMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> updateAddress(
			@PathVariable Long orderId,
			@Valid @RequestBody OrderAddrRequest request,
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return getFieldErrorResponseEntity(bindingResult, "배송 주소 변경 실패");
		}
		validatePathIdWithBody(orderId, request.getOrderId());
		Order order = orderService.updateAddress(request, userDetails.getUser());
		OrderResponse response  = new OrderResponse(order);

		return getResponseEntity(response, "배송 주소 변경 성공");
	}

	/**
	 * 주문 취소
	 */
	@PostMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> cancelOrder(
			@PathVariable Long orderId,
			@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long response = orderService.cancelOrder(orderId, userDetails.getUser());

		return getResponseEntity(response, "주문 취소 성공");
	}

}