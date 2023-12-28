package com.nbc.trello.users;


import com.nbc.trello.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody UserInfoRequestDTO infoRequestDTO) {
        userService.signup(infoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "회원 가입 성공", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody UserRequestDTO userRequestDto, HttpServletResponse response) {
        userService.login(userRequestDto, response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "로그인 성공", null));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        response.setHeader(null, null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "로그아웃 성공", null));
    }


    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> modifyUserPassword(@RequestBody UserPWModifyRequestDTO requestDTO, HttpServletResponse response,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.modifyUserPassword(requestDTO, userDetails.getUser());
        response.setHeader(null, null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "비밀번호 변경 성공", null));
    }
}