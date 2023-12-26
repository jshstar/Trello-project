package com.nbc.trello.global.exception;

import static com.nbc.trello.global.exception.ErrorCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiException> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getStatus()).body(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiException> handleException(RuntimeException e){
        log.error("RuntimeException", e);
        return  ResponseEntity.status(INTERNAL_SERVER_ERROR.getStatus()).body(new ApiException(INTERNAL_SERVER_ERROR));
    }
}
