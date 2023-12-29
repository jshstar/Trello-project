package com.nbc.trello.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 유저
    INVALID_NICKNAME_PASSWORD(400, "닉네임 또는 패스워드를 확인해주세요."),
    INVALID_USERNAME(404, "유저 이름이 올바르지 않습니다."),
    EQUAL_PASSWORD(400, "변경할 비밀번호와 기존 비밀번호가 같습니다."),
	ALREADY_INVITE_USER(400, "이미 초대된 사용자 입니다."),

	// 댓글
    NOT_EXIST_CARD(400, "카드가 존재하지 않습니다."),
    NOT_EXIST_COMMENT(400, "댓글이 존재하지 않습니다."),
    NOT_EQUAL_CREATE_USER(400, "작성자와 본인이 일치하지 않습니다."),

    // 보드
    INVALID_BOARD_ID(404, "보드 id가 올바르지 않습니다."),
    UNAUTHORIZED_BOARD(403, "보드 접근 권한이 없습니다."),
    EXIST_BOARD_USER(409, "이미 보드에 초대한 유저입니다."),

    // 컬럼
    UNAUTHORIZED_COLUMN(403, "컬럼 권한이 없습니다"),
    COLUMNS_NOT_FOUND_EXCEPTION(404, "컬럼을 찾을 수 없습니다."),

	//카드
	INVALID_CARD(404, "찾으시는 카드가 없습니다."),



    INTERNAL_SERVER_ERROR(500, "서버 에러.");

    private final Integer status;
    private final String message;

    ErrorCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
