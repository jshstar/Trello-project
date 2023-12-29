package com.nbc.trello.comment;

import com.nbc.trello.card.entity.Card;
import com.nbc.trello.global.dto.ApiResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, Card card, CommentRequestDto commentRequestDto){
        commentService.createComment(userDetails, card, commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "success", null ));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) throws Exception {
        commentService.updateComment(userDetails,commentId,commentRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null ));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(UserDetailsImpl userDetails, @PathVariable Long commentId) throws Exception {
        commentService.deleteComment(userDetails,commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComment(UserDetailsImpl userDetails, @PathVariable Long cardId) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", commentService.getComment(cardId)));
    }


}
