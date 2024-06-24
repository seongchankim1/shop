package com.sparta.bunga6.product.dto;

import com.sparta.bunga6.product.entity.Product;

public class UpdateProductResponseDto {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    public UpdateProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();

    }
}
