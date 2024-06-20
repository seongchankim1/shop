package com.sparta.bunga6.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

}
