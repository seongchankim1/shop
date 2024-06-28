package com.sparta.shop.dto;

import com.sparta.shop.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequest {

    @NotNull
    private Long userId;

    @NotNull
    private UserRole role;

}
