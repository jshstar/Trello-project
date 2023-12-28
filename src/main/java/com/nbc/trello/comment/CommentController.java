package com.nbc.trello.comment;

import com.nbc.trello.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/columns/{columnId}/cards/{cardId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createComment(User user, Card card, CommentRequestDto commentRequestDto){
        commentService.createComment(user, card, commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "success", null ));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(User user, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) throws Exception {
        commentService.updateComment(user,commentId,commentRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null ));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(User user, @PathVariable Long commentId) throws Exception {
        commentService.deleteComment(user,commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "success", null ));
    }

    @GetMapping
    public void getComment(User user, @PathVariable Long columnId)


}
