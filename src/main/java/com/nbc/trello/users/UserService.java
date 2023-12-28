package com.nbc.trello.users;

import com.nbc.trello.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // adminToken
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(UserInfoRequestDTO infoRequestDTO) {
        String username = infoRequestDTO.getUsername();
        String password = passwordEncoder.encode(infoRequestDTO.getPassword());


        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저 입니다");
        }

        User user = new User(username, password);

        userRepository.save(user);
    }


    public void login(UserRequestDTO requestDTO, HttpServletResponse response) {
        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }
        response.setHeader(JwtUtil.AUTH_HEADER, jwtUtil.createToken(requestDTO.getUsername()));
    }



    public void modifyUserPassword(UserPWModifyRequestDTO requestDTO, User user) {
        String beforePassword = requestDTO.getBeforePassword();
        String afterPassword = requestDTO.getAfterPassword();
        String[] past;

        try {
            past = user.getPastPassword().split(" ");
        } catch (NullPointerException e) {
            past = new String[0];
        }

        if (!passwordEncoder.matches(beforePassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        } else {
            for(int i = 0; i < past.length; i++){
                if(passwordEncoder.matches(afterPassword, past[i])){
                    throw new IllegalArgumentException("최근 3번안에 사용한 비밀번호는 사용할 수 없습니다.");
                }
            }
            afterPassword = passwordEncoder.encode(afterPassword);
            if(past.length == 0){
                user.setPastPassword(user.getPassword());
            } else if(past.length > 2){
                String str = user.getPastPassword().substring(past[0].length() + 1);
                str += " " + user.getPassword();
                user.setPastPassword(str);
            } else {
                user.setPastPassword(user.getPastPassword() + " " + user.getPassword());
            }
            user.setPassword(afterPassword);
        }
        userRepository.save(user);
    }

}