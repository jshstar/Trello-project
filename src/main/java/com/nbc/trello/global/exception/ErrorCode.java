package com.nbc.trello.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 유저
    INVALID_NICKNAME_PASSWORD(400, "닉네임 또는 패스워드를 확인해주세요."),
    EQUAL_PASSWORD(400, "변경할 비밀번호와 기존 비밀번호가 같습니다."),

    // 댓글
    NOT_EXIST_CARD(400, "카드가 존재하지 않습니다."),
    NOT_EXIST_COMMENT(400, "댓글이 존재하지 않습니다."),
    NOT_EQUAL_CREATE_USER(400, "작성자와 본인이 일치하지 않습니다."),


    INTERNAL_SERVER_ERROR(500, "서버 에러.");

    private final Integer status;
    private final String message;

    ErrorCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
