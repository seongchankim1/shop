package com.sparta.shop.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.shop.dto.ProductRequest;
import com.sparta.shop.dto.SignupRequest;
import com.sparta.shop.entity.Like;
import com.sparta.shop.entity.Product;
import com.sparta.shop.entity.Review;
import com.sparta.shop.entity.User;
import com.sparta.shop.entity.UserRole;
import com.sparta.shop.jwt.JwtProvider;
import com.sparta.shop.repository.FollowRepository;
import com.sparta.shop.repository.LikeRepository;
import com.sparta.shop.repository.ProductRepository;
import com.sparta.shop.repository.ReviewRepository;
import com.sparta.shop.repository.UserRepository;
import com.sparta.shop.service.LikeService;

@SpringBootTest
public class LikeServiceTest {

	@Autowired
	private LikeService likeService;

	@Autowired
	private JwtProvider jwtUtil;

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	private MockHttpServletResponse response;
	private String token;
	private User user;
	private Product product;
	private Review review;
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private ReviewRepository reviewRepository;

	@BeforeEach
	void setUp() {
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setUsername("user");
		signupRequest.setPassword("Test123!");
		signupRequest.setName("김유저");
		signupRequest.setAddress("우리집");

		// Create a new User entity
		user = new User(signupRequest, "encodedPassword", UserRole.USER);
		userRepository.save(user);

		response = new MockHttpServletResponse();
		token = jwtUtil.createAccessToken(user.getUsername(), UserRole.USER);
		response.addHeader("Authorization", token);

		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("고기");
		productRequest.setPrice(10000);
		productRequest.setStockQuantity(100);
		product = new Product(productRequest, user);
		productRepository.save(product);

		String content = "리뷰 내용";
		review = Review.of(content, product, user);
		reviewRepository.save(review);
	}

	@AfterEach
	void tearDown() {
		reviewRepository.deleteById(review.getId());
		productRepository.deleteById(product.getId());
		userRepository.deleteById(user.getId());
	}

	@Test
	@DisplayName("상품 좋아요 테스트")
	@Transactional
	void testProductLike() {
		Product newProduct = productRepository.findByName(product.getName());
		Like like = likeService.productLike(newProduct.getId(), user);

		assertEquals(like.getProduct().getName(), newProduct.getName());
		assertEquals(like.getUser().getUsername(), user.getUsername());
	}

	@Test
	@DisplayName("상품 좋아요 예외 테스트")
	@Transactional
	void testProductLikeException() {
		Long invalidProductId = -1L;

		assertThrows(RuntimeException.class, () -> {
			likeService.productLike(invalidProductId, user);
		});
	}

	@Test
	@DisplayName("리뷰 좋아요 테스트")
	@Transactional
	void testReviewLike() {
		String content = "리뷰 내용";
		Review review = reviewRepository.findByContent(content);

		assertEquals(review.getProduct().getName(), product.getName());
		assertEquals(review.getUser().getUsername(), user.getUsername());
	}

	@Test
	@DisplayName("리뷰 좋아요 예외 테스트")
	@Transactional
	void testReviewLikeException() {
		Long invalidReviewId = -1L;

		assertThrows(RuntimeException.class, () -> {
			likeService.reviewLike(invalidReviewId, user);
		});
	}
}
