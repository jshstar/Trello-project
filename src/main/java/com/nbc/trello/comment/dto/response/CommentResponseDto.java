package com.nbc.trello.comment.dto.response;

import com.nbc.trello.comment.entity.Comment;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CommentResponseDto {
    private final String username;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;



    public CommentResponseDto(Comment comment) {
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
