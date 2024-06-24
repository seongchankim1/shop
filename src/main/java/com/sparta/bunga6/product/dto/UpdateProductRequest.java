package com.sparta.bunga6.product.dto;

import lombok.Getter;

@Getter
public class UpdateProductRequest {
    private String Name;
    private int price;
    private int stockQuantity;
}
