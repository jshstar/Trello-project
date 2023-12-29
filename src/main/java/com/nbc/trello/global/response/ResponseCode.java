package com.nbc.trello.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    // 유저

    // 보드
    BOARD_CREATE(HttpStatus.CREATED.value()),
    BOARD_UPDATE(HttpStatus.OK.value())

    ;

    private final Integer status;
    private final String message;

    ResponseCode(Integer status) {
        this.status = status;
        this.message = "성공";
    }
}
