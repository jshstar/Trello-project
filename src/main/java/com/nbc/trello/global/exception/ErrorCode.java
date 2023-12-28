package com.nbc.trello.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 유저
    INVALID_NICKNAME_PASSWORD(404, "닉네임 또는 패스워드를 확인해주세요."),
    INVALID_USERNAME(404, "유저 이름이 올바르지 않습니다."),

    // 보드
    INVALID_BOARD_ID(404, "보드 id가 올바르지 않습니다."),
    UNAUTHORIZED_BOARD(403, "보드 접근 권한이 없습니다."),
    EXIST_BOARD_USER(409, "이미 보드에 초대한 유저입니다."),
    ;

    private final Integer status;
    private final String message;

    ErrorCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
