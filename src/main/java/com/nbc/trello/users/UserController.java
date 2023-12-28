package com.nbc.trello.users;


import com.nbc.trello.global.dto.ApiResponse;
import com.nbc.trello.jwt.JwtUtil;
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
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody UserRequestDTO request) {
        userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "회원 가입 성공", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "로그인 성공", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
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
                .body(ApiResponse.of(HttpStatus.OK.value(), "로그아웃 성공", null));
    }


    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> modifyUserPassword(@RequestBody UserPWModifyRequestDTO requestDTO, HttpServletResponse response,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.modifyUserPassword(requestDTO, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "비밀번호 변경 성공", null));
    }
}