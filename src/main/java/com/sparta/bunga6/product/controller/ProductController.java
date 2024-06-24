package com.sparta.bunga6.product.controller;

import com.sparta.bunga6.base.dto.CommonResponse;
import com.sparta.bunga6.product.dto.*;
import com.sparta.bunga6.product.entity.Product;
import com.sparta.bunga6.product.service.ProductService;
import com.sparta.bunga6.security.UserDetailsImpl;
import com.sparta.bunga6.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.bunga6.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.bunga6.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    ProductService productService;

    @PostMapping
    public RegisterResponseDto regiseterProduct(@RequestBody RegisterRequestDto requsetDto,
                                                                HttpServletResponse response,
                                                                HttpServletRequest request) {
        return productService.registerProduct(requsetDto, response, request);
    }

    @GetMapping
    public List<FindProductResponseDto> findAllProduct() {
        return productService.findAllProduct();
    }

    @GetMapping("/{id}")
    public FindProductResponseDto findProduct(@PathVariable Long id) {
        return productService.findProduct(id);
    }

    @GetMapping("/page/{page}")
    public List<FindProductResponseDto> findPagingProduct(@PathVariable Long page, @RequestBody PagingRequestDto requestDto) {
        return productService.findPagingProduct(page, requestDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id,
                                                @RequestBody UpdateProductRequestDto requestDto,
                                                             HttpServletRequest request) {
        productService.updateProduct(id, requestDto, request);
        return ResponseEntity.ok("상품의 정보를 성공적으로 수정하였습니다!");
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id,
                                             HttpServletRequest request) {
        return productService.deleteProduct(id, request);
    //상품 등록
    @PostMapping
    public ResponseEntity<CommonResponse<?>> regiseterProduct(
            @Valid @RequestBody RegisterRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "상품 등록 실패!");

        }
        Product product = productService.registerProduct(request, userDetails.getUser());
        RegisterResponse response = new RegisterResponse(product);

        return getResponseEntity(response, "상품 등록 성공!");
    }

    //전체 상품 조회
    @GetMapping
    public ResponseEntity<CommonResponse<?>> findAllProduct() {
        List<Product> productList = productService.findAllProduct();
        List<FindProductResponse> responses = productList.stream()
                .map(FindProductResponse::new)
                .collect(Collectors.toList());

        return getResponseEntity(responses, "상품 조회 성공!!");
    }

    //상품 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> findProduct(@PathVariable Long id) {
        FindProductResponse response = productService.findProduct(id);
        return getResponseEntity(response, "상품 조회 성공!!");
    }

    //상품 페이징 조회
    @GetMapping("/page/{page}")
    public List<FindProductResponse> findPagingProduct(@PathVariable Long page,
                                                       @RequestBody PagingRequest requestDto
    ) {
        return productService.findPagingProduct(page, requestDto);
    }

    //상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> updateProduct(
            @Valid @RequestBody UpdateProductRequest request,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "상품 수정 실패");
        }
        UpdateProductResponse response = new UpdateProductResponse(productService.updateProduct(id, request, userDetails.getUser()));
        return getResponseEntity(response, "프로필 수정 성공");
    }


    //상품 삭제
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id,
                                User user) {
        return productService.deleteProduct(id, user);
    }
}
