package com.sparta.bunga6.product.service;

import com.sparta.bunga6.exception.DuplicateNameException;
import com.sparta.bunga6.product.dto.ProductRequest;
import com.sparta.bunga6.product.entity.Product;
import com.sparta.bunga6.product.repository.ProductRepository;
import com.sparta.bunga6.user.entity.User;
import com.sparta.bunga6.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 등록
     */
    @Transactional
    public Product saveProduct(ProductRequest request, User user) {
        validateDuplicateProductName(request.getName());
        return productRepository.save(new Product(request, user));
    }

    /**
     * 전체 상품 조회
     */
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * 상품 조회
     */
    public Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException("해당 상품이 존재하지 않습니다."));
    }

    /**
     * 상품 수정
     */
    @Transactional
    public Product updateProduct(Long productId, ProductRequest request, User user) {
        Product product = findProduct(productId);
        validateDuplicateProductName(request.getName());
        // 관리자가 아닌 일반 회원은 검증
        if (user.getRole().equals(UserRole.USER)) {
            product.verifyUser(user);
        }
        product.update(request);

        return product;
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public Long deleteProduct(Long productId, User user) {
        Product product = findProduct(productId);
        // 관리자가 아닌 일반 회원은 검증
        if (user.getRole().equals(UserRole.USER)) {
            product.verifyUser(user);
        }
        productRepository.delete(product);

        return productId;
    }

    /**
     * 상품명 중복 검증
     */
    private void validateDuplicateProductName(String name) {
        if (productRepository.existsByName(name)) {
            throw new DuplicateNameException("이미 존재하는 상품명입니다.");
        }
    }

}
