package com.inolog.exception.auth;

import com.inolog.exception.InologException;

/**
 * status -> 404
 */
public class UserNotFound extends InologException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
