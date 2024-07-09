package com.inolog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 개발용 패스워드 인코딩
 */
@Profile("test")
@Component
public class PlainPasswordEncoder implements PasswordEncoder {

    @Override
    public String encrypt(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
