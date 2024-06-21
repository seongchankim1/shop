package com.sparta.bunga6.user.dto;

import com.sparta.bunga6.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private UserRole role;

}
