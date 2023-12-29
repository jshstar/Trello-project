package com.nbc.trello.board.controller;

import com.nbc.trello.board.request.BoardCreateRequest;
import com.nbc.trello.board.request.BoardInviteRequest;
import com.nbc.trello.board.request.BoardUpdateRequest;
import com.nbc.trello.board.response.BoardListResponse;
import com.nbc.trello.board.response.BoardResponse;
import com.nbc.trello.board.service.BoardService;
import com.nbc.trello.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBoard(
            @RequestBody BoardCreateRequest request
    ) {
        //boardService.createBoard(user, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "success", null));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequest request
    ) {
        //boardService.updateBoard(user, boardId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long boardId
    ) {
//        boardService.deleteBoard(user, boardId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> findOne(
            @PathVariable Long boardId
    ) {
//        boardService.findOne(user, boardId)

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardListResponse>>> findAll() {
//        boardService.findAll(user)

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null));
    }

    @PostMapping("/{boardId}/invites")
    public ResponseEntity<ApiResponse<Void>> inviteUser(
            @PathVariable Long boardId,
            @RequestBody BoardInviteRequest request
    ) {
//        boardService.inviteUser(user, boardId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null));
    }
}
