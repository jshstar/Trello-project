package com.nbc.trello.card.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CardRequestDto {
	// 제목
	@NotBlank
	private String title;


}
