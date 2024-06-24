package com.sparta.bunga6.review.dto;

import lombok.Getter;

@Getter
public class ReviewResponse {

    private final Long id;
    private final Long productId;
    private final Long userId;
    private final String content;

    public ReviewResponse(Long id, Long productId, Long userId, String content) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.content = content;
    }
}