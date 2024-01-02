package com.nbc.trello.card.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CardRequestDto {
	@NotBlank
	private String title;


}
