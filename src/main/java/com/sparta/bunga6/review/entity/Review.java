package com.sparta.bunga6.review.entity;

import com.sparta.bunga6.product.entity.Product;
import com.sparta.bunga6.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    public Review(String content, User user, Product product) {
        this.content = content;
        this.user = user;
        this.product = product;
    }

    public void setContent(String content) {
        this.content = content;
    }
}