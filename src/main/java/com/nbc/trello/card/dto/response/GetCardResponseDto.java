package com.nbc.trello.card.dto.response;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import com.nbc.trello.card.entity.Card;

import lombok.Getter;

@Getter
public class GetCardResponseDto {

	private final String title;

	private final String description;

	private final LocalDateTime createAt;

	private final String username;

	public GetCardResponseDto(Card card){
		this.title = card.getTitles();
		this.description = card.getDescription();
		this.createAt = card.getCreatedAt();
		this.username = card.getWorker().get(0).getUser().getUsername();
	}

}
