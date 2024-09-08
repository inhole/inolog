package com.inolog.exception.post;

import com.inolog.exception.InologException;

/**
 * status -> 404
 */
public class PostNotFound extends InologException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
