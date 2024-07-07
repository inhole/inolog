package com.inolog.controller;

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
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final static String KEY = "3l3Cw10+O7gzomDsD4c7F1D7v3r3LBJ8MvMx8DJnQKE=";

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        Long userId = authService.signin(login);

//        SecretKey key2 = Jwts.SIG.HS256.key().build();
//        byte[] encodedKey = key2.getEncoded();
//        String strKey = Base64.getEncoder().encodeToString(encodedKey);

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

        String jws = Jwts.builder()
                .subject(String.valueOf(userId))
                .signWith(key)
                .compact();

        return new SessionResponse(jws);
    }
}
