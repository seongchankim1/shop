package com.sparta.bunga6.user.service;

import com.sparta.bunga6.jwt.RefreshTokenRepository;
import com.sparta.bunga6.user.dto.ProfileRequest;
import com.sparta.bunga6.user.dto.RoleRequest;
import com.sparta.bunga6.user.dto.SignupRequest;
import com.sparta.bunga6.user.dto.UpdatePasswordRequest;
import com.sparta.bunga6.user.entity.User;
import com.sparta.bunga6.user.entity.UserRole;
import com.sparta.bunga6.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${admin.token}")
    private String adminToken;

    /**
     * 회원가입
     */
    @Transactional
    public User signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        UserRole role = UserRole.USER;
        if (request.isAdmin()) {
            if (!request.getAdminToken().equals(adminToken)) {
                throw new IllegalArgumentException("어드민 토큰이 일치하지 않습니다.");
            }
            role = UserRole.ADMIN;
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request, encodedPassword, role);
        user.addPasswordToHistory(encodedPassword);

        return userRepository.save(user);
    }

    /**
     * 로그아웃
     */
    @Transactional
    public Long logout(User user) {
        refreshTokenRepository.deleteByUsername(user.getUsername());

        return user.getId();
    }

    /**
     * 전체 회원 조회 (관리자 전용)
     */
    public Page<User> findAllUsers(Pageable pageable, User admin) {
        admin.validateAdmin();
        return userRepository.findAll(pageable);
    }

    /**
     * 회원 조회
     */
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public User updateProfile(Long userId, ProfileRequest request) {
        User user = findUser(userId);
        user.updateProfile(request);

        return user;
    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public User updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = findUser(userId);
        String currentPassword = user.getPassword();

        String oldPassword = request.getOldPassword();
        if (!passwordEncoder.matches(oldPassword, currentPassword)) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String newPassword = request.getNewPassword();
        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new IllegalArgumentException("현재 비밀번호와 동일한 비밀번호로 수정할 수 없습니다.");
        }

        user.updatePassword(passwordEncoder.encode(newPassword));

        return user;
    }

    /**
     * 회원 권한 수정 (관리자 전용)
     */
    @Transactional
    public User updateRole(RoleRequest request, User admin) {
        admin.validateAdmin();
        User user = findUser(request.getUserId());
        user.updateRole(request.getRole());

        return user;
    }

}
