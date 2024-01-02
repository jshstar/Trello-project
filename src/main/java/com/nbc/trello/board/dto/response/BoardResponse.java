package com.nbc.trello.board.dto.response;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.column.dto.response.ColumnResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardResponse {

    private Long id;
    private String name;
    private String description;
    private String backgroundColor;
    private List<ColumnResponse> columns = new ArrayList<>();

    public BoardResponse(Board board, List<ColumnResponse> columns) {
        id = board.getId();
        name = board.getName();
        description = board.getDescription();
        backgroundColor = board.getBackgroundColor();
        this.columns = columns;
    }
}
