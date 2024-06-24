package com.sparta.bunga6.product.dto;

import com.sparta.bunga6.product.entity.Product;

public class UpdateProductResponse {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    public UpdateProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();

    }
}
