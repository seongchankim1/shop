package com.sparta.bunga6.user.entity;

import com.sparta.bunga6.base.entity.Timestamped;
import com.sparta.bunga6.user.dto.ProfileRequest;
import com.sparta.bunga6.user.dto.SignupRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String username; // 사용자 ID

    @Column(nullable = false)
    private String password;

    @ElementCollection
    private List<String> passwordHistory = new ArrayList<>();

    @Column(nullable = false)
    private String name; // 사용자 이름

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private String address; // 추후 임베디드 타입으로 변경

    /**
     * 생성자
     */
    public User(SignupRequest request, String encodedPassword, UserRoleEnum role) {
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

    public List<String> getPasswordHistory() {
        return new ArrayList<>(passwordHistory); // 외부에 직접적인 접근을 막기 위해 복사본 반환 (캡슐화)
    }

}
