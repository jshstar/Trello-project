package com.nbc.trello.card.repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nbc.trello.card.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>{
	// 페이징 조회
	Page<Card> findAllById(Pageable pageable, Long columnsId);


	// 해당 칼럼에 최대 weight값 찾기
	@Query("SELECT MAX(c.weight) FROM Card c WHERE c.columns.id = :columnsId")
	Optional<Double> findMaxWeightByColumnsID(Long columnsId);

	// 해당 칼럼의 weight 기준 정렬 리스트 출력
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
