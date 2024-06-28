package com.sparta.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank
    private String name;

    @NotNull
    private int price;

    @NotNull
    private int stockQuantity;

}
