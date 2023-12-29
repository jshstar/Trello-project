package com.nbc.trello.column.dto;

import com.nbc.trello.card.entity.Card;
import com.nbc.trello.column.entity.Columns;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ColumnResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Card> cards = new ArrayList<>(); // TODO: DTO로 수정하자

    public ColumnResponse(Columns columns) {
        this.id = columns.getId();
        this.name = columns.getColumnsName();

    }
}
