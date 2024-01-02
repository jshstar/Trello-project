package com.nbc.trello.card.dto.response;

import java.time.LocalDateTime;

import com.nbc.trello.card.entity.Card;

import lombok.Getter;

@Getter
public class PageCardDtoWrapper {
	private final String title;

	private final LocalDateTime createAt;

	public PageCardDtoWrapper(Card card){
		this.title = card.getTitles();
		this.createAt = card.getCreatedAt();
	}
}
