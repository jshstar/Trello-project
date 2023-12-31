package com.nbc.trello.board.controller;

import com.nbc.trello.board.request.BoardCreateRequest;
import com.nbc.trello.board.request.BoardInviteRequest;
import com.nbc.trello.board.request.BoardUpdateRequest;
import com.nbc.trello.board.response.BoardListResponse;
import com.nbc.trello.board.response.BoardResponse;
import com.nbc.trello.board.service.BoardService;
import com.nbc.trello.global.dto.EmptyObject;
import com.nbc.trello.global.response.ApiResponse;
import com.nbc.trello.board.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    public static final String SUCCESS = "success";
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmptyObject>> createBoard(
            @RequestBody BoardCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        boardService.createBoard(userDetails.getUser(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), SUCCESS));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<EmptyObject>> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.updateBoard(userDetails.getUser(), boardId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), SUCCESS));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<EmptyObject>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.deleteBoard(userDetails.getUser(), boardId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> findOne(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponse board = boardService.findOne(userDetails.getUser(), boardId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), SUCCESS, board));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardListResponse>>> findAll(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<BoardListResponse> boards = boardService.findAll(userDetails.getUser());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), SUCCESS, boards));
    }

    @PostMapping("/{boardId}/invites")
    public ResponseEntity<ApiResponse<EmptyObject>> inviteUser(
            @PathVariable Long boardId,
            @RequestBody BoardInviteRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.inviteUser(userDetails.getUser(), boardId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), SUCCESS));
    }
}
