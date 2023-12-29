package com.nbc.trello.worker.entity;

import com.nbc.trello.users.User;
import com.nbc.trello.card.entity.Card;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Worker {
	@EmbeddedId
	private WorkerID id;

	@Setter
	@MapsId("cardId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "card_id")
	private Card card;

	@Setter
	@JoinColumn(name = "user_id")
	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public Worker(Card card, User user){
		this.card = card;
		this.user = user;
	}
}
