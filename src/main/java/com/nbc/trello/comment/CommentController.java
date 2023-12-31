package com.nbc.trello.comment;

import com.nbc.trello.board.service.BoardService;
import com.nbc.trello.card.entity.Card;
import com.nbc.trello.card.service.CardService;
import com.nbc.trello.global.response.ApiResponse;
import com.nbc.trello.users.UserDetailsImpl;
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
    public ResponseEntity<ApiResponse<Void>> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId,
        @PathVariable Long cardId,
        @RequestBody CommentRequestDto commentRequestDto
    ) {
        boardService.checkAuthorization(userDetails.getUser(), boardId);
        commentService.createComment(userDetails, cardId, commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "success", null));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto
    ) throws Exception {
        boardService.checkAuthorization(userDetails.getUser(), boardId);
        commentService.updateComment(userDetails, commentId, commentRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null));
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
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null));
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
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", commentResponseDto));
    }
}
