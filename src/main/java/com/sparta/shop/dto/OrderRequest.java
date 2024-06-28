package com.sparta.shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull
    private Long productId;

    @NotNull
    private int count;

}
