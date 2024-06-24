package com.sparta.bunga6.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderAddrRequest {

    @NotNull
    private Long orderId;

    @NotBlank
    private String address;

}
