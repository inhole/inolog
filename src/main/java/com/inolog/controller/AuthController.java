package com.inolog.controller;

import com.inolog.domain.User;
import com.inolog.exception.InvalidRequest;
import com.inolog.exception.InvalidSigninInformation;
import com.inolog.repository.UserRepository;
import com.inolog.request.Login;
import com.inolog.response.SessionResponse;
import com.inolog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Login login) {
        String accessToken = authService.signin(login);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") // TODO 서버 환경에 따른 분리 필요
                .path("/") // Cookie 헤더를 전송하기 위하여 요청되는 URL 내에 반드시 존재해야 하는 URL 경로
                .httpOnly(true) // Cross-site 스크립팅 공격을 방지하기 위한 옵션
                .secure(false) // HTTPS 프로토콜 상에서 암호화된 요청을 위한 옵션
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict") // 서로 다른 도메인간의 쿠키 전송에 대한 보안을 설정
                .build();
        log.info(">>>>>cookie={}", cookie.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
