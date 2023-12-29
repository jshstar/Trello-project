package com.nbc.trello.board.response;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.column.dto.ColumnResponse;
import com.nbc.trello.column.entity.Columns;
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
