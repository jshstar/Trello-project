package com.nbc.trello.board.response;

import com.nbc.trello.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponse {

    private Long id;
    private String name;

    public BoardListResponse(Board board) {
        id = board.getId();
        name = board.getName();
    }
}
