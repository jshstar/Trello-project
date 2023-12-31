package com.nbc.trello.card.dto.response;

import org.springframework.data.domain.Page;

import com.nbc.trello.card.entity.Card;

import lombok.Getter;

@Getter
public class PageCardResponseDto {

	private final Page<PageCardDtoWrapper> pageCard;

	public PageCardResponseDto(Page<Card> pageCard){
		this.pageCard = pageCard.map(PageCardDtoWrapper::new);

	}
}
