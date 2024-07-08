package com.inolog.controller;

import com.inolog.config.AppConfig;
import com.inolog.domain.User;
import com.inolog.exception.InvalidRequest;
import com.inolog.exception.InvalidSigninInformation;
import com.inolog.repository.UserRepository;
import com.inolog.request.Login;
import com.inolog.response.SessionResponse;
import com.inolog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        Long userId = authService.signin(login);

        // 암호화 키 생성
//        SecretKey key2 = Jwts.SIG.HS256.key().build();
//        byte[] encodedKey = key2.getEncoded();
//        String strKey = Base64.getEncoder().encodeToString(encodedKey);

        // JWS(Json Web Signature) 생성
        String jws = Jwts.builder()
                .subject(String.valueOf(userId))
                .signWith(appConfig.getJwtKey())
                .issuedAt(new Date()) // 생성 일
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 유효 기간 1시간
                .compact();

        return new SessionResponse(jws);
    }
}
