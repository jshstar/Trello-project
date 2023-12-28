package com.nbc.trello.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 유저
    INVALID_NICKNAME_PASSWORD(400, "닉네임 또는 패스워드를 확인해주세요."),
    INVALID_BOARD_USER(400, "접근할 수 없는 사용자 입니다."),
    INVALID_CARD(404, "찾으시는 카드가 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 에러.");

    private final Integer status;
    private final String message;

    ErrorCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
