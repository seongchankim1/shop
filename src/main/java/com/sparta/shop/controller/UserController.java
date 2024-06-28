package com.sparta.shop.controller;

import com.sparta.shop.base.dto.CommonResponse;
import com.sparta.shop.security.UserDetailsImpl;
import com.sparta.shop.dto.*;
import com.sparta.shop.entity.User;
import com.sparta.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.sparta.shop.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원 가입
     */
    @PostMapping("/user/signup")
    public ResponseEntity<CommonResponse<?>> signup(
            @Valid @RequestBody SignupRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원가입 실패");
        }

        User user = userService.signup(request);
        SignupResponse response = new SignupResponse(user);

        return getResponseEntity(response, "회원 가입 성공");
    }

    /**
     * 로그아웃
     */
    @GetMapping("/user/logout")
    public ResponseEntity<CommonResponse<?>> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = userService.logout(userDetails.getUser());

        return getResponseEntity(response, "로그아웃 성공");
    }

    /**
     * 프로필 조회
     */
    @GetMapping("/profiles/{userId}")
    public ResponseEntity<CommonResponse<?>> getProfile(
            @PathVariable Long userId
    ) {
        User user = userService.findUser(userId);
        ProfileResponse response = new ProfileResponse(user);

        return getResponseEntity(response, "프로필 조회 성공");
    }

    /**
     * 프로필 수정
     */
    @PostMapping("/profiles/{userId}")
    public ResponseEntity<CommonResponse<?>> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "프로필 수정 실패");
        }
        validateUser(userId, userDetails);

        User user = userService.updateProfile(userId, request);
        ProfileResponse response = new ProfileResponse(user);

        return getResponseEntity(response, "프로필 수정 성공");
    }

    /**
     * 비밀번호 수정
     */
    @PatchMapping("/profiles/{userId}")
    public ResponseEntity<CommonResponse<?>> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "비밀번호 변경 실패");
        }
        validateUser(userId, userDetails);

        User user = userService.updatePassword(userId, request);
        ProfileResponse response = new ProfileResponse(user);

        return getResponseEntity(response, "비빌번호 변경 성공");
    }

    /**
     * 전체 회원 조회 (관리자 전용)
     */
    @GetMapping("/users")
    public ResponseEntity<CommonResponse<?>> getAllUsers(
            @PageableDefault(
                    sort = "createdAt",
                    size = 5,
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<User> page = userService.findAllUsers(pageable, userDetails.getUser());
        Page<ProfileResponse> response = page.map(ProfileResponse::new);

        return getResponseEntity(response, "전체 회원 조회 성공");
    }

    /**
     * 회원 권한 수정 (관리자 전용)
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<CommonResponse<?>> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody RoleRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원 권한 수정 실패");
        }
        validatePathIdWithBody(userId, request.getUserId());

        User user = userService.updateRole(request, userDetails.getUser());
        ProfileResponse response = new ProfileResponse(user);

        return getResponseEntity(response, "회원 권한 수정 성공");
    }

    private void validateUser(Long userId, UserDetailsImpl userDetails) {
        if (!Objects.equals(userId, userDetails.getUser().getId())) {
            throw new IllegalArgumentException("해당 id의 회원이 아닙니다.");
        }
    }

}
