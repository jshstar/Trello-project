package com.nbc.trello.board.response;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.column.Columns;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardResponse {

    private Long id;
    private String name;
    private String description;
    private String backgroundColor;
    private List<Columns> columns; // TODO: 적당한 DTO로 수정

    public BoardResponse(Board board) {
        id = board.getId();
        name = board.getName();
        description = board.getDescription();
        backgroundColor = board.getBackgroundColor();
    }
}
