package com.nbc.trello.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbc.trello.users.UserDetailsImpl;
import com.nbc.trello.users.UserRequestDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/");
    }

    @Override
    public Authentication attemptAuthentication (
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserRequestDTO requestDto = new ObjectMapper().readValue(request.getInputStream(), UserRequestDTO.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    // 인증완료시 액세스토큰, 리프레시토큰 생성
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authen)
            throws IOException, ServletException {

        String username = ((UserDetailsImpl) authen.getPrincipal()).getUsername();


        String accessToken = jwtUtil.createToken(username);
        jwtUtil.addAccessTokenToCookie(accessToken, response);

        String refToken = jwtUtil.createRefreshToken(username);
        jwtUtil.addRefreshTokenToCookie(refToken, response);

        response.setHeader(JwtUtil.AUTH_HEADER,accessToken);
        System.out.println("success");
    }

    @Override
    protected void unsuccessfulAuthentication (HttpServletRequest request, HttpServletResponse response,
                                               AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }


}