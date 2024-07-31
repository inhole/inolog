package com.inolog.controller;

import com.inolog.exception.InologException;
import com.inolog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception 관리 컨트롤러
 * Enum 활용?
 */
@Slf4j
@ControllerAdvice
public class ExceptionController {

/*
    # ControllerAdvice 동작 과정
    1. 디스패처 서블릿이 에러를 catch함
    2. 해당 에러를 처리할 수 있는 처리기(HandlerExceptionResolver)가 에러를 처리함
    3. 컨트롤러의 ExceptionHandler로 처리가능한지 검사함
    4. ControllerAdvice의 ExceptionHandler로 처리가능한지 검사함
    5. ControllerAdvice의 ExceptionHandler 메소드를 invoke하여 예외를 반환함
*/

    /**
     * MethodArgumentNotValidException 의 예외처리
     * 400 에러코드
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response =  ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    /**
     * RuntimeException 의 예외처리
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(InologException.class)
    public ResponseEntity<ErrorResponse> inologException(InologException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response =  ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }

    /**
     * 추가적인 예외발생에 대한 처리
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("예외발생", e);

        ErrorResponse body = ErrorResponse.builder()
                .code("500")
                .message(e.getMessage())
                .build();

        ResponseEntity<ErrorResponse> response =  ResponseEntity.status(500)
                .body(body);

        return response;
    }

}
