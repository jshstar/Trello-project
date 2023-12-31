package com.nbc.trello.column.dto;

import com.nbc.trello.card.dto.response.CardResponseDto;
import com.nbc.trello.card.entity.Card;
import com.nbc.trello.column.entity.Columns;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ColumnResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CardResponseDto> cards = new ArrayList<>();

    public ColumnResponse(Columns columns) {
        this.id = columns.getId();
        this.name = columns.getColumnsName();
        this.createdAt = columns.getCreatedAt();
        this.modifiedAt = columns.getModifiedAt();
        this.cards = columns.getCards().stream().map(CardResponseDto::new).toList();
    }
}
