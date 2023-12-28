package com.nbc.trello.card.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nbc.trello.card.dto.request.CardRequestDto;
import com.nbc.trello.card.dto.request.CardUpdateRequestDto;
import com.nbc.trello.card.dto.response.CardResponseDto;
import com.nbc.trello.card.dto.response.GetCardResponseDto;
import com.nbc.trello.card.dto.response.MoveCardResponseDto;
import com.nbc.trello.card.dto.response.PageCardResponseDto;
import com.nbc.trello.card.dto.response.UpdateCardResponseDto;
import com.nbc.trello.card.service.CardService;
import com.nbc.trello.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class CardController {

	private final CardService cardService;

	@PostMapping("/{boardId}/columns/{columnId}/cards")
	public ResponseEntity<ApiResponse<CardResponseDto>> createCard(@PathVariable Long boardId,
										@PathVariable Long columnId,
										@RequestBody CardRequestDto cardRequestDto,
										@AuthenticationPrincipal UserDetailsImpl userDetails){
		CardResponseDto cardResponseDto = cardService.createCard(boardId, columnId, cardRequestDto, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.of(HttpStatus.CREATED.value(),"카드 생성 성공", cardResponseDto));
	}

	// 카드 업데이트
	@PutMapping("/{boardId}/columns/{columnId}/cards/{cardId}")
	public ResponseEntity<ApiResponse<UpdateCardResponseDto>> updateCard(@PathVariable Long boardId,
									@PathVariable Long columnId,
									@PathVariable Long cardId,
									@RequestBody CardUpdateRequestDto cardUpdateRequestDto,
									@AuthenticationPrincipal UserDetailsImpl userDetails){
		UpdateCardResponseDto updateCardResponseDto = cardService.updateCard(boardId, columnId, cardId, cardUpdateRequestDto, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.of(HttpStatus.OK.value(),"카드 업데이트 성공", updateCardResponseDto));
	}

	// 카드 페이징 조회
	@GetMapping("/{boardID}/columns/{columnId}/cards")
	public ResponseEntity<ApiResponse<PageCardResponseDto>> getPageCard(@PathVariable Long boardID,
										@PathVariable Long columnId,
										Pageable pageable,
										@AuthenticationPrincipal UserDetailsImpl userDetails
										){
		PageCardResponseDto pageCardResponseDto = cardService.getPageCard(boardID, columnId, pageable, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.of(HttpStatus.OK.value(),"카드 페이징 조회 성공", pageCardResponseDto));;
	}

	// 카드 단건조회
	@GetMapping("/{boardID}/columns/{columnId}/cards/{cardId}")
	public ResponseEntity<ApiResponse<GetCardResponseDto>> getCard(@PathVariable Long boardID,
										@PathVariable Long columnId,
										@PathVariable Long cardId,
										@AuthenticationPrincipal UserDetailsImpl userDetails
										){
		GetCardResponseDto getCardResponseDto = cardService.getCard(boardID, columnId, cardId, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.of(HttpStatus.OK.value(),"카드 조회 성공", getCardResponseDto));
	}

	// 삭제처리
	@DeleteMapping("/{boardId}/columns/{columnId}/cards/{cardId}")
	public ResponseEntity<ApiResponse<Void>> deleteCard(@PathVariable Long boardId,
										@PathVariable Long columnId,
										@PathVariable Long cardId,
										@AuthenticationPrincipal UserDetailsImpl userDetails){
		cardService.deleteCard(boardId, columnId, cardId, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.of(HttpStatus.OK.value(),"카드 삭제 성공",null));
	}

	// 칼럼 이동
	@PostMapping("/{boardId}/columns/{columnId}/cards/{cardId}/moveColumn/{moveColumnId}/moveCard/{moveCardId}")
	public ResponseEntity<ApiResponse<MoveCardResponseDto>> moveCardToColumn(@PathVariable Long boardId,
											@PathVariable Long columnId,
											@PathVariable Long cardId,
											@PathVariable Long moveColumnId,
											@PathVariable Long moveCardId,
											@AuthenticationPrincipal UserDetailsImpl userDetails){
		MoveCardResponseDto moveCardResponseDto =
			cardService.moveCardToColumn(boardId, columnId, cardId, moveColumnId,moveCardId, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.of(HttpStatus.OK.value(),"칼럼 이동 성공",moveCardResponseDto));
	}
	@PostMapping("/{boardId}/columns/{columnId}/cards/{cardId}/move/{moveCardId}")
	public ResponseEntity<ApiResponse<MoveCardResponseDto>> moveCard(@PathVariable Long boardId,
		 															 @PathVariable Long columnId,
		  															 @PathVariable Long cardId,
		  															 @PathVariable Long moveCardId,
		 															 @AuthenticationPrincipal UserDetailsImpl userDetails){
		MoveCardResponseDto moveCardResponseDto =
			cardService.moveCard(boardId, columnId, cardId, moveCardId, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.of(HttpStatus.OK.value(), "카드 이동 성공",moveCardResponseDto));

	}

	// 작업자 지정
	@PostMapping("/{boardId}/columns/{columnId}/cards/{cardId}/worker/{userId}")
	public ResponseEntity<ApiResponse<Void>> createWorkerCard(@PathVariable Long boardId,
											@PathVariable Long columnId,
											@PathVariable Long cardId,
											@PathVariable Long userId,
											@AuthenticationPrincipal UserDetailsImpl userDetails){
		cardService.inviteWorkerToCard(boardId, columnId, cardId, userId, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.of(HttpStatus.OK.value(), "작업자 초대 성공", null));
	}

}
