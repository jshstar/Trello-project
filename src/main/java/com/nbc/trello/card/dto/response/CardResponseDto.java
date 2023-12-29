package com.nbc.trello.card.dto.response;

import java.time.LocalDateTime;

import com.nbc.trello.card.entity.Card;

import lombok.Getter;

@Getter
public class CardResponseDto {
	private final String title;

	private final LocalDateTime createAt;

	private final String username;
	public CardResponseDto(Card card){
		this.title = card.getTitles();
		this.username = card.getWorker().get(0).getUser().getUsername();
		this.createAt = card.getCreatedAt();
	}


}
