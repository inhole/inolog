package com.inolog.crypto;

@Deprecated
public interface PasswordEncoder {

    String encrypt(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
