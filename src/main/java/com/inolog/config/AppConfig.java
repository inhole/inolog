package com.inolog.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.util.Base64;

@Data
@ConfigurationProperties(prefix = "inolog")
public class AppConfig {

    private SecretKey jwtKey;

    public void setJwtKey(String jwtKey) {
        // 암호화 키 디코딩
        this.jwtKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey));
    }

    public SecretKey getJwtKey() {
        return jwtKey;
    }
}
