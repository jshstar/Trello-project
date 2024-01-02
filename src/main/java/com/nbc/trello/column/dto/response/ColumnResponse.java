package com.nbc.trello.column.dto.response;

import com.nbc.trello.card.dto.response.CardResponseDto;
import com.nbc.trello.card.entity.Card;
import com.nbc.trello.column.entity.Columns;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ColumnResponse {

    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final List<CardResponseDto> cards;

    public ColumnResponse(Columns columns) {
        this.id = columns.getId();
        this.name = columns.getColumnsName();
        this.createdAt = columns.getCreatedAt();
        this.modifiedAt = columns.getModifiedAt();
        this.cards = columns.getCards().stream().map(CardResponseDto::new).toList();
    }
}
