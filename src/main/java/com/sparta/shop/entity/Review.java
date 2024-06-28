package com.sparta.shop.entity;

import com.sparta.shop.base.entity.Timestamped;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 정적 팩토리 메서드
     */
    public static Review of(String content, Product product, User user) {
        Review review = new Review();
        review.product = product;
        review.user = user;
        review.content = content;

        return review;
    }

    public void update(String content) {
        this.content = content;
    }

    public void verifyProduct(Long productId) {
        if (!this.product.getId().equals(productId)) {
            throw new IllegalStateException("상품 ID가 일치하지 않습니다.");
        }
    }

    public void verifyUser(Long userId) {
        if (!this.id.equals(userId)) {
            throw new IllegalStateException("리뷰 작성자가 일치하지 않습니다.");
        }
    }

}