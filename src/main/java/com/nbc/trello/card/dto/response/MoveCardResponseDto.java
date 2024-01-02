package com.nbc.trello.card.dto.response;

import com.nbc.trello.card.entity.Card;

import lombok.Getter;

@Getter
public class MoveCardResponseDto {

	private final String title;

	private final Long columnPosition;

	private final Long cardPosition;

	public MoveCardResponseDto(Card card, Long moveCardId){
		this.title = card.getTitles();
		this.columnPosition = card.getColumns().getId();
		this.cardPosition = moveCardId;
	}

}
