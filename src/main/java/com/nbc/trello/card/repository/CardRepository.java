package com.nbc.trello.card.repository;

import static com.nbc.trello.global.exception.ErrorCode.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nbc.trello.card.entity.Card;
import com.nbc.trello.global.exception.ApiException;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>{
	// 페이징 조회
	@Query("select c from Card c where c.columns.id = :columnsId order by c.weight")
	Page<Card> findWeightCardPage(Pageable pageable, Long columnsId);


	// 해당 칼럼에 최대 weight값 찾기
	@Query("SELECT MAX(c.weight) FROM Card c WHERE c.columns.id = :columnsId")
	Optional<Double> findMaxWeightByColumnsID(Long columnsId);

	// 해당 칼럼의 weight 기준 정렬 리스트 출력
	@Query("select c from Card c where c.columns.id = :columnsId order by c.weight")
	List<Card> findWeightCardList(Long columnsId);

	// 찾으려는 카드 출력
	default Optional<Card> findCard(Long columnsId, Long cardId){
		List<Card> cards = findWeightCardList(columnsId);
		if(cards.size() >= cardId){
			for (Card card: cards) {
				if(Objects.equals(card.getId(), cardId)) {
					return Optional.of(card);
				}
			}
		}
		return Optional.empty();
	}

	// 해당하는 카드 삭제
	default void deleteCard(Long columnsId, Long cardId){
		List<Card> cards = findWeightCardList(columnsId);
		boolean deleteFlag = false;
		if(cards.size() >= cardId){
			for (Card deletecard: cards) {
				if(Objects.equals(deletecard.getId(), cardId)) {
					delete(deletecard);
					deleteFlag = true;
					break;
				}
			}

			if(!deleteFlag){
				throw new ApiException(INVALID_DELETE_CARD);
			}

		}
	}

}
