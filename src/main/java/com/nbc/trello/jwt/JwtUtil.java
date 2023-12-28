package com.nbc.trello.jwt;

import com.nbc.trello.user.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_HEADER = "RefreshToken";
    public static final String REFRESH_KEY = "refresh";

    private final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24;  // 24시간
    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7;  // 7일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 액세스 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        if (role == UserRoleEnum.ROLE_ADMIN)
            return BEARER_PREFIX +
                    Jwts.builder()
                            .claim(AUTH_KEY,"ROLE_ADMIN")
                            .setSubject(username) // 사용자 식별자값(ID)
                            .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRATION)) // 만료 시간  1일
                            .setIssuedAt(date)
                            .signWith(key, signatureAlgorithm)
                            .compact();
        else
            return BEARER_PREFIX +
                    Jwts.builder()
                            .claim(AUTH_KEY,"ROLE_USER")
                            .setSubject(username) // 사용자 식별자값(ID)
                            .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRATION)) // 만료 시간  1일
                            .setIssuedAt(date)
                            .signWith(key, signatureAlgorithm)
                            .compact();
    }

    // JWT Cookie 에 저장
    public void addAccessTokenToCookie (String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
            Cookie cookie = new Cookie(AUTH_HEADER, token);
            cookie.setPath("/");

            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String username, UserRoleEnum role) {
        Date date = new Date();

        if (role == UserRoleEnum.ROLE_ADMIN)
            return BEARER_PREFIX +
                    Jwts.builder()
                            .claim(AUTH_KEY,"ROLE_ADMIN")
                            .setSubject(username) // 사용자 식별자값(ID)
                            .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRATION)) // 만료 시간  7일
                            .setIssuedAt(date)
                            .signWith(key, signatureAlgorithm)
                            .compact();
        else
            return BEARER_PREFIX +
                    Jwts.builder()
                            .claim(AUTH_KEY,"ROLE_USER")
                            .setSubject(username) // 사용자 식별자값(ID)
                            .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRATION)) // 만료 시간  7일
                            .setIssuedAt(date)
                            .signWith(key, signatureAlgorithm)
                            .compact();
    }

    // 리프레시토큰  쿠키에 저장
    public void addRefreshTokenToCookie(String refToken, HttpServletResponse res) {
        try {
            refToken = URLEncoder.encode(refToken, "utf-8").replaceAll("\\+", "%20");
            Cookie cookie = new Cookie(REFRESH_HEADER, refToken);
            cookie.setPath("/");

            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // 헤더 토큰 유무 확인
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) throws ExpiredJwtException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUserInfoFromTokenByString(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTH_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}