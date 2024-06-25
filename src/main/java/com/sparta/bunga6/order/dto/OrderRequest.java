package com.sparta.bunga6.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull
    private Long productId;

    @NotNull
    private int count;

}
