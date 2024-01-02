package com.nbc.trello.comment.controller;

import com.nbc.trello.board.service.BoardService;
import com.nbc.trello.comment.dto.request.CommentRequestDto;
import com.nbc.trello.comment.dto.response.CommentResponseDto;
import com.nbc.trello.comment.service.CommentService;
import com.nbc.trello.global.response.ApiResponse;
import com.nbc.trello.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/columns/{columnId}/cards/{cardId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final BoardService boardService;
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId,
        @PathVariable Long cardId,
        @RequestBody CommentRequestDto commentRequestDto
    ) {
        boardService.checkAuthorization(userDetails.getUser(), boardId);
        CommentResponseDto commentResponseDto = commentService.createComment(userDetails, cardId, commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "댓글 생성 성공",  commentResponseDto));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto
    ) throws Exception {
        boardService.checkAuthorization(userDetails.getUser(), boardId);
        CommentResponseDto commentResponseDto = commentService.updateComment(userDetails, commentId, commentRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "댓글 수정 성공", commentResponseDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) throws Exception {
        boardService.checkAuthorization(userDetails.getUser(), boardId);
        commentService.deleteComment(userDetails, commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "댓글 삭제 성공", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @PathVariable Long cardId) throws Exception {
        boardService.checkAuthorization(userDetails.getUser(), boardId);
        List<CommentResponseDto> commentResponseDto = commentService.getComment(cardId);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "댓글 목록 조회 성공", commentResponseDto));
    }
}
