package com.nbc.trello.users.controller;


import com.nbc.trello.security.UserDetailsImpl;
import com.nbc.trello.global.dto.EmptyObject;
import com.nbc.trello.global.response.ApiResponse;
import com.nbc.trello.jwt.JwtUtil;
import com.nbc.trello.users.dto.request.UserPWModifyRequestDto;
import com.nbc.trello.users.dto.request.UserRequestDto;
import com.nbc.trello.users.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<EmptyObject>> signup(@Valid @RequestBody UserRequestDto request) {
        userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "회원 가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<EmptyObject>> login() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "로그인 성공"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<EmptyObject>> logout(HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(
                        HttpHeaders.SET_COOKIE,
                        ResponseCookie.
                                from(JwtUtil.AUTH_HEADER, "")
                                .maxAge(1)
                                .path("/")
                                .build()
                                .toString()
                )
                .body(ApiResponse.of(HttpStatus.OK.value(), "로그아웃 성공"));
    }


    @PutMapping("/password")
    public ResponseEntity<ApiResponse<EmptyObject>> modifyUserPassword(@RequestBody UserPWModifyRequestDto requestDTO, HttpServletResponse response,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.modifyUserPassword(requestDTO, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "비밀번호 변경 성공"));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<EmptyObject>> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.deleteUser(userDetails.getUser(), userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
