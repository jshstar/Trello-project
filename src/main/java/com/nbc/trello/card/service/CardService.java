package com.nbc.trello.card.service;

import static com.nbc.trello.global.exception.ErrorCode.*;

import java.util.List;
import org.hibernate.annotations.Columns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbc.trello.card.Repository.CardRepository;
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
import com.nbc.trello.global.exception.ApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

	private final CardRepository cardRepository;

	private final BoardRepository boardRepository; // boardService 변경 예정

	private final ColumnsRepository columnsRepository; //columnsService 변경 예정

	private final UserRepository userRepository; // userService 변경예정

	@Transactional
	public CardResponseDto createCard(Long boardId, Long columnId, CardRequestDto cardRequestDto, User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);
		Columns columns = columnsRepository.findById(columnId).orElseThrow(() -> new ApiException(INVALID_CARD));
		Double maxWeight = findMaxWeightAndCheckNull(columnId)+1.0;
		Card card = new Card(cardRequestDto, maxWeight); // columns
		// solution : 케스케이드 설정이 돼있기 때문에 worker를 만들어 등록만 시켜주면 알아서 영속성이 끝나기전에 임시로 받은 ID값이 worker로 들어가 저장된다.
		card.createWorker(user);
		Card saveCard = cardRepository.save(card);
		return new CardResponseDto(saveCard);
	}

	@Transactional
	public UpdateCardResponseDto updateCard(Long boardId, Long columnId, Long cardId, CardUpdateRequestDto cardUpdateRequestDto,
		User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user); // board
		Columns columns = columnsRepository.findById(columnId).orElseThrow(() -> new ApiException(INVALID_CARD));
		Card card = cardRepository.findById(cardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		card.updateCard(cardUpdateRequestDto, columns);
		return new UpdateCardResponseDto(card);
	}

	// 카드 페이징 조회
	public PageCardResponseDto getPageCard(Long boardId, Long columnId, Pageable pageable, User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);

		Sort sort = pageable.getSort();
		Sort additionalSort = Sort.by(Sort.Direction.ASC, "weight");
		Sort finalSort = sort.and(additionalSort);
		Pageable addFinalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), finalSort);

		Page<Card> pageCardList = cardRepository.findAllById(addFinalPageable, columnId);
		return new PageCardResponseDto(pageCardList);
	}

	// 카드 선택 조회
	public GetCardResponseDto getCard(Long boardId, Long columnId, Long cardId, User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);
		Card card = cardRepository.findCard(columnId, cardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		return new GetCardResponseDto(card);
	}

	// 카드 삭제
	public void deleteCard(Long boardId, Long columnId, Long cardId, User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);
		cardRepository.deleteCard(columnId, cardId);
	}

	// 카드 칼럼 이동
	@Transactional
	public MoveCardResponseDto moveCard(Long boardId, Long columnId, Long cardId, MoveCardRequestDto moveCardRequestDto, User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);
		Card card = ChecklistAndRunCardAction(columnId, cardId, moveCardRequestDto);

		cardRepository.save(card);
		return new MoveCardResponseDto(card, moveCardRequestDto.getCardPosition());
	}

	@Transactional
	// 작업자 초대
	public void inviteWorkerToCard(Long boardId, Long columnId, Long cardId, InviteUserRequestDto inviteUserRequestDto, User user){
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);

		User inviteUser = userRepository.findById(inviteUserRequestDto.getUsername())
			.orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, inviteUser);

		Card card = cardRepository.findCard(columnId, cardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		card.createWorker(inviteUser);
	}

	// 칼럼에 카드 있는지 체크후 동작 실행
	public Card ChecklistAndRunCardAction(Long columnId, Long cardId, MoveCardRequestDto moveCardRequestDto){
		List<Card> cardList = cardRepository.findWeightCardList(moveCardRequestDto.getColumnsPosition());
		Card card = cardRepository.findCard(columnId, cardId).orElseThrow(() -> new ApiException(INVALID_CARD));

		if (cardList.isEmpty()) {
			// 칼럼 정보 가져오기
			// Columns columns = cardRepository.findById(columnId).orElseThrow(() -> new ApiException(INVALID_CARD)).getColumns();
			Columns columns = columnsRepository.findById(moveCardRequestDto.getColumnsPosition())
				.orElseThrow(() -> new ApiException(INVALID_CARD));
			card.addColumn(columns);
			card.updateCardWeight(1.0);
		} else {
			Columns moveColumn = cardList.get(0).getColumns();
			card = calculateWeightMoveCard(card, moveCardRequestDto.getCardPosition() , cardList);
			card.addColumn(moveColumn);
		}
		return card;
	}



	// 옮기려는 카드, 옮길 카드 위치
	private Card calculateWeightMoveCard(Card card, Long moveCardPosition, List<Card> cardList) {
		// cardList값이 있는데 첫번째 칸에 들어가는 경우
		double calculateWeight = 0;

		if (moveCardPosition == 1) { // 옮기려는 카드 위치가 첫번째 일때

			Card nextCard = cardList.get(0);
			calculateWeight = nextCard.getWeight() / 2;

		} else if (moveCardPosition >= cardList.size()) { // 옮기려는 카드 위치가 마지막 일때

			Double moveColumnMaxWeight = cardList.get(cardList.size() - 1).getWeight();
			calculateWeight = moveColumnMaxWeight+1;

		} else { // 그 외 경우

			calculateWeight = (cardList.get(moveCardPosition.intValue() - 1).getWeight()
				+ cardList.get(moveCardPosition.intValue() + 1).getWeight()) / 2;

		}
		card.updateCardWeight(calculateWeight);
		return card;
	}


	public Double findMaxWeightAndCheckNull(Long columnId){
		return cardRepository.findMaxWeightByColumnsID(columnId).orElse(1.0);
	}

	// 사용자 체크
	public void boardUserCheck(Board board, User user) { // Board board, User user
		if (!board.getUser().getId().equals(user.getId())) {
			throw new ApiException(INVALID_BOARD_USER);
		}
	}

}
