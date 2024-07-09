package com.inolog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("default")
@Component
public class ScryptPasswordEncoder implements PasswordEncoder {

    private static final SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
            16,
            8,
            1,
            32,
            64);

    /**
     * 패스워드 암호화
     * @param rawPassword
     * @return
     */
    @Override
    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 패스워드 검증
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }


}
