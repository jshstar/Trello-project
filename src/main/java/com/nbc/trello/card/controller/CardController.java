package com.nbc.trello.card.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nbc.trello.card.service.CardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class CardController {

	private final CardService cardService;

	// @PostMapping("/{boardId}/columns/{columnId}/cards")
	// public ResponseEntity<?> createCard(@PathVariable Long boardId,
	// 									@PathVariable Long columnId,
	// 									@RequestBody CardRequestDto cardRequestDto,
	// 									@AuthenticationPrincipal UserDetailsImpl userDetails){
	//
	// 		return null;
	// }

	// 카드 업데이트
	// @PutMapping("/{boardId}/columns/{columnId}/cards/{cardId}")
	// public ResponseEntity<?> update(@PathVariable Long boardId,
	// 								@PathVariable Long columnId,
	// 								@PathVariable Long cardId,
	// 								@RequestBody CardUpdateRequestDto cardUpdateRequestDto,
	// 								@AuthenticationPrincipal UserDetailsImpl userDetails){
	//
	// 	return null;
	// }

	// 카드 페이징 조회
	// @GetMapping("/{boardID}/columns/{columnId}/cards")
	// public ResponseEntity<?> getPageCard(@PathVariable Long boardID,
	// 									@PathVariable Long columnId,
	// 									Pageable pageable,
	// 									@AuthenticationPrincipal UserDetailsImpl userDetails
	// 									){
	//
	// 	return null;
	// }

	// 카드 단건조회
	// @GetMapping("/{boardID}/columns/{columnId}/cards/{cardId}")
	// public ResponseEntity<?> getPageCard(@PathVariable Long boardID,
	// 									@PathVariable Long columnId,
	// 									@PathVariable Long cardId,
	// 									@AuthenticationPrincipal UserDetailsImpl userDetails
	// 									){
	//
	// 	return null;
	// }

	// 삭제처리
	// @DeleteMapping("/{boardId}/columns/{columnId}/cards/{cardId}")
	// public ResponseEntity<?> deleteCard(@PathVariable Long boardId,
	// 									@PathVariable Long columnId,
	// 									@PathVariable Long cardId,
	// 									@AuthenticationPrincipal UserDetailsImpl userDetails){
	// 	return null;
	// }

	// 칼럼 이동
	// @PostMapping("/{boardId}/columns/{columnId}/cards/{cardId}/move/{columnedId}")
	// public ResponseEntity<?> moveColumnCard(@PathVariable Long boardId,
	// 										@PathVariable Long columnId,
	// 										@PathVariable Long cardId,
	// 										@PathVariable Long columnedId,
	// 										@AuthenticationPrincipal UserDetailsImpl userDetails){
	//
	// 	return null;
	// }

	// 작업자 지정
	// @PostMapping("/{boardId}/columns/{columnId}/cards/{cardId}/worker/{userId}")
	// public ResponseEntity<?> createWorkerCard(@PathVariable Long boardId,
	// 										@PathVariable Long columnId,
	// 										@PathVariable Long cardId,
	// 										@PathVariable Long userId,
	// 										@AuthenticationPrincipal UserDetailsImpl userDetails){
	// 	return null
	// }

}
