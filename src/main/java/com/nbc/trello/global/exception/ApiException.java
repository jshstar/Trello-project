package com.nbc.trello.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiException extends RuntimeException {

    private Integer status;

    private String message;

    public ApiException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
