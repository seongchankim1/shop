package com.sparta.bunga6.product.dto;

import com.sparta.bunga6.product.entity.Product;

public class FindProductResponse {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    public FindProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
    }
}
