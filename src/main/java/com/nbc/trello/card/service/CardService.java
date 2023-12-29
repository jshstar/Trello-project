package com.nbc.trello.card.service;

import static com.nbc.trello.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbc.trello.User.repository.UserRepository;
import com.nbc.trello.board.entity.Board;
import com.nbc.trello.board.repository.BoardRepository;
import com.nbc.trello.card.Repository.CardRepository;
import com.nbc.trello.card.dto.request.CardRequestDto;
import com.nbc.trello.card.dto.request.CardUpdateRequestDto;
import com.nbc.trello.card.dto.response.CardResponseDto;
import com.nbc.trello.card.dto.response.GetCardResponseDto;
import com.nbc.trello.card.dto.response.MoveCardResponseDto;
import com.nbc.trello.card.dto.response.PageCardResponseDto;
import com.nbc.trello.card.dto.response.UpdateCardResponseDto;
import com.nbc.trello.card.entity.Card;
import com.nbc.trello.columns.entity.Columns;
import com.nbc.trello.columns.repository.ColumnsRepository;
import com.nbc.trello.global.exception.ApiException;
import com.nbc.trello.users.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

	private final CardRepository cardRepository;

	private final BoardRepository boardRepository;

	private final ColumnsRepository columnsRepository;

	private final UserRepository userRepository;

	@Transactional
	public CardResponseDto createCard(Long boardId, Long columnId, CardRequestDto cardRequestDto, User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);
		Columns columns = columnsRepository.findById(columnId).orElseThrow(() -> new ApiException(INVALID_CARD));
		Card card = new Card(cardRequestDto, columns); // columns
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
		Sort additionalSort = Sort.by(Sort.Direction.DESC, "weight");
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
	public MoveCardResponseDto moveCardToColumn(Long boardId, Long columnId, Long cardId, Long moveColumnId, Long moveCardId, User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);
		List<Card> cardList = cardRepository.findWeightCardList(moveColumnId);
		Card card = cardRepository.findCard(columnId, cardId).orElseThrow(() -> new ApiException(INVALID_CARD));

		if (cardList.isEmpty()) {
			Columns columns = columnsRepository.findById(moveColumnId).orElseThrow(() -> new ApiException(INVALID_CARD));
			card.moveColumn(columns);
			card.updateCardWeight(columns.increaseMaxWeight());
		} else {
			Columns moveColumn = cardList.get(0).getColumns();
			card = calculateWeightMoveCard(card, moveCardId, cardList);
			card.moveColumn(moveColumn);
		}

		cardRepository.save(card);
		return new MoveCardResponseDto(card, moveCardId);
	}

	// 카드 위치 이동
	public MoveCardResponseDto moveCard(Long boardId, Long columnId, Long cardId, Long moveCardId, User user) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);
		List<Card> cardList = cardRepository.findWeightCardList(columnId);
		if (cardList.isEmpty()){
			throw new ApiException(INVALID_CARD);
		}
		Card moveCard = cardList.get(cardId.intValue());
		moveCard = calculateWeightMoveCard(moveCard, moveCardId, cardList);
		cardRepository.save(moveCard);

		return new MoveCardResponseDto(moveCard, moveCardId);
	}

	// 작업자 초대
	public void inviteWorkerToCard(Long boardId, Long columnId, Long cardId, Long userId, User user){
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, user);
		User inviteUser = userRepository.findById(userId).orElseThrow(() -> new ApiException(INVALID_CARD));
		boardUserCheck(board, inviteUser);
		Card card = cardRepository.findCard(columnId, cardId).orElseThrow(() -> new ApiException(INVALID_CARD));
		card.createWorker(inviteUser);
	}




	// 옮기려는 카드, 옮길 카드 위치
	private Card calculateWeightMoveCard(Card movecard, Long moveCardId, List<Card> cardList) {
		// cardList값이 있는데 첫번째 칸에 들어가는 경우
		double calculateWeight = 0;
		if (moveCardId == 1) { // 옮기려는 카드 위치가 첫번째 일때
			Card nextCard = cardList.get(0);
			calculateWeight = nextCard.getWeight() / 2;
		} else if (moveCardId >= cardList.size()) { // 옮기려는 카드 위치가 마지막 일때
			Long moveColumnMaxWeight = cardList.get(0).getColumns().getMaxWeight();
			calculateWeight = (cardList.get(cardList.size() - 1).getWeight() + moveColumnMaxWeight + 1) / 2;
		} else { // 그 외 경우
			calculateWeight = (cardList.get(moveCardId.intValue() - 1).getWeight()
				+ cardList.get(moveCardId.intValue() + 1).getWeight()) / 2;
		}
		movecard.updateCardWeight(calculateWeight);
		return movecard;
	}

	// 사용자 체크
	public void boardUserCheck(Board board, User user) { // Board board, User user
		if (!board.getUser().getId().equals(user.getId())) {
			throw new ApiException(INVALID_BOARD_USER);
		}
	}

}
