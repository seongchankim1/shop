package com.sparta.bunga6.product.entity;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.exception.NotEnoughStockException;
import com.sparta.bunga6.product.dto.ProductRequest;
import com.sparta.bunga6.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 상품명

    @Column(nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockQuantity; // 재고수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 생성자
     */
    public Product(ProductRequest request, User user) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.stockQuantity = request.getStockQuantity();
        this.user = user;
    }

    /**
     * 재고 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
    }

    /**
     * 사용자 검증
     */
    public void verifyUser(User user) {
        if (!user.getId().equals(this.user.getId())) {
            throw new IllegalArgumentException("해당 상품을 등록한 사용자가 아닙니다.");
        }
    }

    /**
     * 상품 정보 수정
     */
    public void update(ProductRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.stockQuantity = request.getStockQuantity();
    }

}

