package com.inolog.util;

import com.inolog.config.AppConfig;
import com.inolog.config.data.UserSession;
import com.inolog.exception.Unauthorized;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final AppConfig appConfig;

    /**
     * JWS(Json Web Signature) 생성
     * @param userId
     * @return
     */
    public String generateToken(Long userId) {
        // 암호화 키 생성
//        SecretKey key2 = Jwts.SIG.HS256.key().build();
//        byte[] encodedKey = key2.getEncoded();
//        String strKey = Base64.getEncoder().encodeToString(encodedKey);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .signWith(appConfig.getJwtKey())
                .issuedAt(new Date()) // 생성 일
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 유효 기간 1시간
                .compact();
    }

    /**
     * Claims 추출/검증
     * @param jws
     * @return
     */
    public Claims parseToken(String jws) {

        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        try {
            Jws<Claims> claims =  Jwts.parser()
                    .verifyWith(appConfig.getJwtKey())
                    .build()
                    .parseSignedClaims(jws);
            return claims.getPayload();
        } catch (JwtException e) {
            log.error("error :: {} ", e.getMessage());
            throw new Unauthorized();
        }
    }

    /**
     * UserId 추출
     * @param jws
     * @return
     */
    public Long getUserId(String jws) {
        Claims claims = parseToken(jws);
        return Long.parseLong(claims.getSubject());
    }

}
