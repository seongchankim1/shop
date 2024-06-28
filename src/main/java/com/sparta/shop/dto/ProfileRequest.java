package com.sparta.shop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

}
