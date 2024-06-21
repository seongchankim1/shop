package com.sparta.bunga6.user.entity;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.user.dto.ProfileRequest;
import com.sparta.bunga6.user.dto.SignupRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 사용자 ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name; // 사용자 이름

    @Column(nullable = false)
    private String address;

    /**
     * 생성자
     */
    public User(SignupRequest request, String encodedPassword) {
        this.username = request.getUsername();
        this.password = encodedPassword;
        this.name = request.getName();
        this.address = request.getAddress();
    }

    /**
     * 프로필 수정
     */
    public void updateProfile(ProfileRequest request) {
        this.name = request.getName();
        this.address = request.getAddress();
    }

    /**
     * 비밀번호 수정
     */
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    /**
     * 검증 메서드
     */
    public void verifyUser(Long userId) {
        if (!this.id.equals(userId)) {
            throw new IllegalArgumentException("사용자의 id가 해당 id와 다릅니다.");
        }
    }

}
