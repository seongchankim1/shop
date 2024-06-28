package com.sparta.shop.dto;

import com.sparta.shop.entity.Product;
import lombok.Data;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
    }

}
