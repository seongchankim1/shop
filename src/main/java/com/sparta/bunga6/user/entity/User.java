package com.sparta.bunga6.user.entity;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.user.dto.ProfileRequest;
import com.sparta.bunga6.user.dto.SignupRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    private static final int PASSWORD_HISTORY_LIMIT = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address; // 추후 임베디드 타입으로 변경

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role; // 사용자 권한 [USER, ADMIN]

    @ElementCollection
    private List<String> passwordHistory = new ArrayList<>();

    /**
     * 생성자
     */
    public User(SignupRequest request, String encodedPassword, UserRole role) {
        this.username = request.getUsername();
        this.password = encodedPassword;
        this.name = request.getName();
        this.address = request.getAddress();
        this.role = role;
    }

    /**
     * 새로운 비밀번호 히스토리에 저장
     */
    public void addPasswordToHistory(String newPassword) {
        if (passwordHistory.size() == PASSWORD_HISTORY_LIMIT) {
            passwordHistory.remove(0); // 가장 오래된 비밀번호 제거
        }
        passwordHistory.add(newPassword);
    }

    /**
     * 비밀번호 수정
     */
    public void updatePassword(String password) {
        if (isPasswordInHistory(password)) {
            throw new IllegalArgumentException("최근에 사용한 비밀번호로는 변경할 수 없습니다.");
        }
        this.password = password;
    }

    /**
     * 최근에 변경한 비밀번호인지 확인
     */
    public boolean isPasswordInHistory(String password) {
        return passwordHistory.contains(password);
    }

    /**
     * 프로필 수정
     */
    public void updateProfile(ProfileRequest request) {
        this.name = request.getName();
        this.address = request.getAddress();
    }

    /**
     * 권한 수정
     */
    public void updateRole(UserRole role) {
        this.role = role;
    }

    public List<String> getPasswordHistory() {
        return new ArrayList<>(passwordHistory); // 외부에 직접적인 접근을 막기 위해 복사본 반환 (캡슐화)
    }

}