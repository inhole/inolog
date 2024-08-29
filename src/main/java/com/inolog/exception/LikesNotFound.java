package com.inolog.exception;

/**
 * status -> 404
 */
public class LikesNotFound extends InologException {

    private static final String MESSAGE = "존재하지 않는 좋아요입니다.";

    public LikesNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
