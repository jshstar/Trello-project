package com.nbc.trello.card.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class CardUpdateRequestDto {
	private String title;

	private String description;

	private String color;
}
