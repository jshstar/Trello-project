package com.nbc.trello.card.dto.request;

import lombok.Getter;

@Getter
public class MoveCardRequestDto {
	private Long columnsPosition;

	private Long cardPosition;
}
