package com.nbc.trello.card.dto.response;

import com.nbc.trello.card.entity.Card;

import lombok.Getter;

@Getter
public class MoveCardResponseDto {

	private final String title;

	private final Long columnId;

	private final Long cardId;

	public MoveCardResponseDto(Card card, Long moveCardId){
		this.title = card.getTitles();
		this.columnId = card.getColumns().getId();
		this.cardId = moveCardId;
	}

}
