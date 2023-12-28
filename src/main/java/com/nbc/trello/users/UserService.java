package com.nbc.trello.users;

import com.nbc.trello.global.exception.ApiException;
import com.nbc.trello.global.exception.ErrorCode;
import com.nbc.trello.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public void signup(UserRequestDTO request) {
        String username = request.getUsername();
        String password = passwordEncoder.encode(request.getPassword());

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 유저 입니다");
        }

        User user = new User(username, password);
        userRepository.save(user);
    }

    @Transactional
    public void modifyUserPassword(UserPWModifyRequestDTO requestDTO, User user) {
        User savedUser = userRepository.save(user);
        String beforePassword = requestDTO.getBeforePassword();
        String afterPassword = requestDTO.getAfterPassword();
        if (!beforePassword.equals(afterPassword)) {
            throw new ApiException(ErrorCode.EQUAL_PASSWORD);
        }

        if (!passwordEncoder.matches(beforePassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        } else {
            savedUser.setPassword(afterPassword);
        }
    }

}