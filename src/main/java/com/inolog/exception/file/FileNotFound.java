package com.inolog.exception.file;

import com.inolog.exception.InologException;

public class FileNotFound extends InologException {

    private static final String MESSAGE = "파일이 비어있습니다.";

    public FileNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
