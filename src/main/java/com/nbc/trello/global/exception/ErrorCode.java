package com.nbc.trello.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 유저
    INVALID_NICKNAME_PASSWORD(400, "닉네임 또는 패스워드를 확인해주세요."),

    ;

    private final Integer status;
    private final String message;

    ErrorCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
