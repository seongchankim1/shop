package com.sparta.bunga6.product.dto;

import com.sparta.bunga6.product.entity.Product;

public class FindProductResponseDto {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    public FindProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
    }
}
