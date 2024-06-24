package com.sparta.bunga6.product.controller;

import com.sparta.bunga6.base.dto.CommonResponse;
import com.sparta.bunga6.product.dto.ProductRequest;
import com.sparta.bunga6.product.dto.ProductResponse;
import com.sparta.bunga6.product.entity.Product;
import com.sparta.bunga6.product.service.ProductService;
import com.sparta.bunga6.security.UserDetailsImpl;
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

import static com.sparta.bunga6.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.bunga6.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> saveProduct(
            @Valid @RequestBody ProductRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "상품 등록 실패");
        }
        Product product = productService.saveProduct(request, userDetails.getUser());
        ProductResponse response = new ProductResponse(product);

        return getResponseEntity(response, "상품 등록 성공");
    }

    /**
     * 전체 상품 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> findAllProducts(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Page<Product> page = productService.findAllProducts(pageable);
        Page<ProductResponse> response = page.map(ProductResponse::new);

        return getResponseEntity(response, "전체 상품 조회 성공");
    }

    /**
     * 상품 조회
     */
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<?>> findProduct(
            @PathVariable Long productId
    ) {
        Product product = productService.findProduct(productId);
        ProductResponse response = new ProductResponse(product);

        return getResponseEntity(response, "상품 조회 성공");
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{productId}")
    public ResponseEntity<CommonResponse<?>> updateProduct(
            @Valid @RequestBody ProductRequest request,
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "상품 수정 실패");
        }
        Product product = productService.updateProduct(productId, request, userDetails.getUser());
        ProductResponse response = new ProductResponse(product);

        return getResponseEntity(response, "상품 수정 성공");
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse<?>> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = productService.deleteProduct(productId, userDetails.getUser());

        return getResponseEntity(response, "상품 삭제 성공");
    }

}