package com.nbc.trello.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {

    private String name;
    private String backgroundColor;
    private String description;
}
