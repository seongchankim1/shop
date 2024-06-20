package com.sparta.bunga6.user.dto;

import com.sparta.bunga6.user.entity.User;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProfileResponse {

    private Long userId;
    private String username;
    private String name;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProfileResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.address = user.getAddress();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

}
