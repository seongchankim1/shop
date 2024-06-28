package com.sparta.shop.dto;

import com.sparta.shop.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignupResponse {

    private Long id;
    private String username;
    private String name;
    private String address;
    private LocalDateTime createdAt;

    public SignupResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.address = user.getAddress();
        this.createdAt = user.getCreatedAt();
    }

}