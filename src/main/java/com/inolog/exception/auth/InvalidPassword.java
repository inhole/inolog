package com.inolog.exception.auth;

import com.inolog.exception.InologException;

public class InvalidPassword extends InologException {

    private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";

    public InvalidPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
