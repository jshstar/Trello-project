package com.nbc.trello.card.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Columns;

import com.nbc.trello.card.dto.request.CardRequestDto;
import com.nbc.trello.card.dto.request.CardUpdateRequestDto;
import com.nbc.trello.global.entity.BaseEntity;
import com.nbc.trello.worker.entity.Worker;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "card")
@NoArgsConstructor
public class Card extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 카드 이름
	@Column(name = "titles", length = 30)
	private String titles;

	// 카드 설명
	@Column(name = "description", length = 500)
	private String description;

	// 카드 색상
	@Column(name = "colors", length = 10)
	private String colors;

	@ManyToOne
	@JoinColumn(name = "columns_id")
	private Columns columns;

	@OneToMany(mappedBy = "card",cascade = CascadeType.ALL , orphanRemoval = true)
	private List<Worker> worker;

	@OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	private double weight;

	public Card(CardRequestDto cardRequestDto , double weight){ // columns
		this.titles = cardRequestDto.getTitle();
		this.worker = new ArrayList<>();
		this.weight = weight;
	}

	public void createWorker(User user){
		Worker worker = new Worker(this, user);
		this.worker.add(worker);
	}
	public void updateCard(CardUpdateRequestDto cardUpdateRequestDto, Columns columns){
		this.titles = cardUpdateRequestDto.getTitle();
		this.colors = cardUpdateRequestDto.getColor();
		this.description = cardUpdateRequestDto.getDescription();
		this.columns = columns;
	}

	public void updateCardWeight(double weight){
		this.weight = weight;
	}

	public void addColumn(Columns columns){
		this.columns = columns;
	}

	public void addComment(Comment comment){
		this.comments.add(comment);
	}


}
