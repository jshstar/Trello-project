package com.nbc.trello.card.dto.response;

import java.time.LocalDateTime;

import com.nbc.trello.card.entity.Card;

import lombok.Getter;

@Getter
public class UpdateCardResponseDto {
	private final String title;

	private final String description;

	private final String colors;

	private final LocalDateTime modifiedAt;


	public UpdateCardResponseDto(Card card){
		this.title = card.getTitles();
		this.description = card.getDescription();
		this.colors = card.getColors();
		this.modifiedAt = card.getModifiedAt();
	}
}
