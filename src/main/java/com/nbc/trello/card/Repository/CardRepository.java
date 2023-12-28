package com.nbc.trello.card.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nbc.trello.card.entity.Card;
import com.nbc.trello.columns.entity.Columns;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>{
	// 페이징 조회
	Page<Card> findAllById(Pageable pageable, Long columnsId);

	//
	@Query("select c from Card c where c.columns.id = :columnsId order by c.weight")
	List<Card> findWeightCardList(Long columnsId);

	default Optional<Card> findCard(Long columnsId, Long cardId){
		List<Card> cards = findWeightCardList(columnsId);
		if(cards.size() >= cardId){
			return Optional.of(cards.get(cardId.intValue()-1));
		}
		return Optional.empty();
	}

	default void deleteCard(Long columnsId, Long cardId){
		List<Card> cards = findWeightCardList(columnsId);
		if(cards.size() >= cardId){
			Card deleteCard = cards.get(cardId.intValue() - 1);
			delete(deleteCard);
		}
	}

}
