package com.nbc.trello.user;

import com.nbc.trello.CommonResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDTO> signup(@Valid @RequestBody UserInfoRequestDTO infoRequestDTO) {
        try {
            userService.signup(infoRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED.value())
                    .body(new CommonResponseDTO("회원 가입 성공", HttpStatus.CREATED.value()));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new CommonResponseDTO("중복된 username", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDTO> login(@RequestBody UserRequestDTO userRequestDto, HttpServletResponse response) {
        try {
            userService.login(userRequestDto, response);
            return ResponseEntity.ok().body(new CommonResponseDTO("로그인 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<CommonResponseDTO> logout(HttpServletResponse response) {
        response.setHeader(null, null);
        return ResponseEntity.ok().body(new CommonResponseDTO("로그아웃 성공", HttpStatus.OK.value()));
    }


    @PutMapping("/profile/{userId}/password")
    public ResponseEntity<CommonResponseDTO> modifyUserPassword(@PathVariable Long userId, @RequestBody UserPWModifyRequestDTO requestDTO, HttpServletResponse response, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            userService.modifyUserPassword(requestDTO, userDetails.getUser());
            response.setHeader(null, null);
            return ResponseEntity.ok().body(new CommonResponseDTO("비밀번호 변경 성공", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CommonResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}