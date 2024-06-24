package com.sparta.bunga6.product.dto;

import com.sparta.bunga6.product.entity.Product;

public class RegisterResponse {
    public RegisterResponse(Product createProduct) {

        Long id;
        String name;
        int price;
        int stockQuantity;
    }
}
