package com.sparta.shop.dto;

import com.sparta.shop.entity.Review;
import lombok.Data;

@Data
public class ReviewResponse {

    private final Long id;
    private final String content;
    private final Long productId;
    private final Long userId;
    private final String username;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.productId = review.getProduct().getId();
        this.userId = review.getUser().getId();
        this.username = review.getUser().getName();
    }
}