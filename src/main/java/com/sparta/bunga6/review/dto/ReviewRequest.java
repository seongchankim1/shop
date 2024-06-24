package com.sparta.bunga6.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Long productId;
    private String content;
}