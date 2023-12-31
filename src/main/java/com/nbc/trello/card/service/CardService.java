package com.nbc.trello.card.service;

import static com.nbc.trello.global.exception.ErrorCode.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbc.trello.board.service.BoardService;
import com.nbc.trello.card.dto.request.CardRequestDto;
import com.nbc.trello.card.dto.request.CardUpdateRequestDto;
import com.nbc.trello.card.dto.request.InviteUserRequestDto;
import com.nbc.trello.card.dto.request.MoveCardRequestDto;
import com.nbc.trello.card.dto.response.CardResponseDto;
import com.nbc.trello.card.dto.response.GetCardResponseDto;
import com.nbc.trello.card.dto.response.MoveCardResponseDto;
import com.nbc.trello.card.dto.response.PageCardResponseDto;
import com.nbc.trello.card.dto.response.UpdateCardResponseDto;
import com.nbc.trello.card.entity.Card;
import com.nbc.trello.card.repository.CardRepository;
import com.nbc.trello.column.entity.Columns;
import com.nbc.trello.column.repository.ColumnsRepository;
import com.nbc.trello.global.exception.ApiException;
import com.nbc.trello.users.User;
import com.nbc.trello.users.UserRepository;
import com.nbc.trello.worker.repository.WorkerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

	private final CardRepository cardRepository;

	private final BoardService boardService;

	private final ColumnsRepository columnsRepository;

	private final UserRepository userRepository;

	private final WorkerRepository workerRepository;



	// 카드 생성
	@Transactional
	public CardResponseDto createCard(Long boardId, Long columnId, CardRequestDto cardRequestDto, User user) {
		User saveUser = userRepository.save(user);
		boardService.checkAuthorization(saveUser, boardId);
		Columns columns = columnsRepository.findById(columnId)
			.orElseThrow(() -> new ApiException(COLUMNS_NOT_FOUND_EXCEPTION));
		Double maxWeight = findMaxWeightAndCheckNull(columnId)+1.0;
		Card card = new Card(cardRequestDto, maxWeight);
		card.addColumn(columns);
		card.createWorker(saveUser);
		Card saveCard = cardRepository.save(card);
		return new CardResponseDto(saveCard);
	}

	// 카드 업데이트
	@Transactional
	public UpdateCardResponseDto updateCard(Long boardId, Long columnId, Long cardId, CardUpdateRequestDto cardUpdateRequestDto,
		User user) {
		boardService.checkAuthorization(user, boardId);
		Columns columns = columnsRepository.findById(columnId)
			.orElseThrow(() -> new ApiException(COLUMNS_NOT_FOUND_EXCEPTION));
		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new ApiException(INVALID_CARD));
		card.updateCard(cardUpdateRequestDto, columns);
		return new UpdateCardResponseDto(card);
	}

	// 카드 페이징 조회
	public PageCardResponseDto getPageCard(Long boardId, Long columnId, Pageable pageable, User user) {
		boardService.checkAuthorization(user, boardId);

		Sort sort = pageable.getSort();
		Sort additionalSort = Sort.by(Sort.Direction.ASC, "weight");
		Sort finalSort = sort.and(additionalSort);
		Pageable addFinalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), finalSort);

		Page<Card> pageCardList = cardRepository.findWeightCardPage(addFinalPageable, columnId);
		return new PageCardResponseDto(pageCardList);
	}

	// 카드 선택 조회
	public GetCardResponseDto getCard(Long boardId, Long columnId, Long cardId, User user) {
		boardService.checkAuthorization(user, boardId);
		Card card = cardRepository.findCard(columnId, cardId)
			.orElseThrow(() -> new ApiException(INVALID_CARD));
		return new GetCardResponseDto(card);
	}

	// 카드 삭제
	public void deleteCard(Long boardId, Long columnId, Long cardId, User user) {
		boardService.checkAuthorization(user, boardId);
		cardRepository.deleteCard(columnId, cardId);
	}

	// 카드 칼럼 이동
	@Transactional
	public MoveCardResponseDto moveCard(Long boardId, Long columnId, Long cardId, MoveCardRequestDto moveCardRequestDto, User user) {
		boardService.checkAuthorization(user, boardId);
		Card card = ChecklistAndRunCardAction(columnId, cardId, moveCardRequestDto);

		cardRepository.save(card);
		return new MoveCardResponseDto(card, moveCardRequestDto.getCardPosition());
	}

	// 작업자 초대
	@Transactional
	public void inviteWorkerToCard(Long boardId, Long columnId, Long cardId, InviteUserRequestDto inviteUserRequestDto, User user){
		boardService.checkAuthorization(user, boardId);

		User inviteUser = userRepository.findByUsername(inviteUserRequestDto.getUsername())
			.orElseThrow(() -> new ApiException(INVALID_USERNAME));
		boardService.checkAuthorization(user, boardId);

		Card card = cardRepository.findCard(columnId, cardId)
			.orElseThrow(() -> new ApiException(INVALID_CARD));
		checkWorker(card, inviteUser);
	}

	// 칼럼에 카드 있는지 체크후 동작 실행
	public Card ChecklistAndRunCardAction(Long columnId, Long cardId, MoveCardRequestDto moveCardRequestDto){
		List<Card> cardList = cardRepository.findWeightCardList(moveCardRequestDto.getColumnsPosition());
		Card card = cardRepository.findCard(columnId, cardId)
			.orElseThrow(() -> new ApiException(INVALID_CARD));

		if (cardList.isEmpty()) {
			Columns columns = columnsRepository.findById(moveCardRequestDto.getColumnsPosition())
				.orElseThrow(() -> new ApiException(COLUMNS_NOT_FOUND_EXCEPTION));
			card.addColumn(columns);
			card.updateCardWeight(1.0);

		} else {
			Columns moveColumn = cardList.get(0).getColumns();
			card = calculateWeightMoveCard(card, moveCardRequestDto.getCardPosition() , cardList);
			card.addColumn(moveColumn);

		}

		return card;
	}


	// 카드 위치에 따른 weight값 계산
	private Card calculateWeightMoveCard(Card card, Long moveCardPosition, List<Card> cardList) {

		double calculateWeight;
		Long currentCardID = card.getId();
		boolean moveCardUnderCheck = currentCardPositionCompareMovePosition(currentCardID, moveCardPosition, cardList);

		if (moveCardPosition == 1) { // 옮기려는 카드 위치가 첫번째 일때

			Card nextCard = cardList.get(0);
			calculateWeight = nextCard.getWeight() / 2;

		} else if (moveCardPosition >= cardList.size()) { // 옮기려는 카드 위치가 마지막 일때

			Double moveColumnMaxWeight = cardList.get(cardList.size() - 1).getWeight();
			calculateWeight = moveColumnMaxWeight+1;

		} else { // 그 외 경우
			if(moveCardUnderCheck)
			{
				calculateWeight = (cardList.get(moveCardPosition.intValue() -1).getWeight() +
					cardList.get(moveCardPosition.intValue()).getWeight())/2;

			} else {

				calculateWeight = (cardList.get(moveCardPosition.intValue() - 2).getWeight()
					+ cardList.get(moveCardPosition.intValue()-1).getWeight()) / 2;

			}


		}
		card.updateCardWeight(calculateWeight);
		return card;
	}


	// 동일 칼럼 안에 있는 카드의 위치가 옮기려는 카드의 위치보다 작은경우 체크
	private boolean currentCardPositionCompareMovePosition(Long currentCardId, Long moveCardPosition, List<Card> cardList){
		boolean moveCardUnderCheck = false;
		for (Card value : cardList) {
			if (Objects.equals(currentCardId, value.getId())) {
				if (currentCardId < moveCardPosition - 1) {
					moveCardUnderCheck = true;
					break;
				}
			}
		}
		return moveCardUnderCheck;
	}


	// 작업자 명단에 초대한 유저가 있는지 확인
	private void checkWorker(Card card, User user){
		boolean userWorkerFlag = workerRepository.existsByCardIdAndUserId(card.getId(), user.getId());
		if(!userWorkerFlag){
			card.createWorker(user);
		} else {
			throw new ApiException(ALREADY_INVITE_USER);
		}
	}

	public Card findCard(Long cardId){
		return cardRepository.findById(cardId).orElseThrow(
			() -> new ApiException(INVALID_CARD));
	}


	// wieght값 체크 여부
	public Double findMaxWeightAndCheckNull(Long columnId){
		return cardRepository.findMaxWeightByColumnsID(columnId).orElse(1.0);
	}


}
