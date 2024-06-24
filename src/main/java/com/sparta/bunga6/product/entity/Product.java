package com.sparta.bunga6.product.entity;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.product.dto.RegisterRequest;
import com.sparta.bunga6.product.dto.UpdateProductRequest;
import com.sparta.bunga6.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Product extends Timestamped {


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime writeDate;    // 생성일자

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 상품이름

    @Column(nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockQuantity; // 재고수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column
    private String username;

    public Product(RegisterRequest request, User user) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.stockQuantity = request.getStockQuantity();
        this.username = user.getUsername();
    }

    public void updateProduct(UpdateProductRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.stockQuantity = request.getStockQuantity();
        this.username = user.getUsername();
    }
}

