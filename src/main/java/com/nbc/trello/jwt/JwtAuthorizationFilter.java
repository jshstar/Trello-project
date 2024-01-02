package com.nbc.trello.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbc.trello.global.response.ApiResponse;
import com.nbc.trello.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getTokenFromRequest(request);

        if(Objects.nonNull(token)){
            token = jwtUtil.substringToken(token);
            if(jwtUtil.validateToken(token)){
                Claims info = jwtUtil.getUserInfoFromToken(token);
                // 인증에 유저정보(username) 넣기
                // username -> user 조회
                System.out.println(info);
                String username = info.getSubject();
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // userDetails에 저장
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                // authentication의 principal에 저장
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 저장한 내용을 securityContent에 저장
                context.setAuthentication(authentication);
                // securityContent를 SecurityContextHolder에 저장
                SecurityContextHolder.setContext(context);
                // 위 작업으로 인해 @AuthenticationPrincipal로 조회 가능
            } else {
                // 인증 토큰 없을떄
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(
                        ApiResponse.of(HttpStatus.BAD_REQUEST.value(),"토큰이 유효하지 않습니다.",null)));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
