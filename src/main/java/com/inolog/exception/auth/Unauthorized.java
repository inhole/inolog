package com.inolog.exception.auth;


import com.inolog.exception.InologException;

/**
 * status -> 401
 */
public class Unauthorized extends InologException {

    private static final String MESSAGE = "인증이 필요합니다.";


    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
