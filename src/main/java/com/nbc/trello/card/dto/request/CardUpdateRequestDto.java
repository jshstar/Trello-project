package com.nbc.trello.card.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardUpdateRequestDto {
	private String title;

	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime deadline;

	private String color;
}
