package com.nbc.trello.comment;

import com.nbc.trello.comment.Comment;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private String username;
    private String cardTitle;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifedAt;



    public CommentResponseDto(Comment comment) {
        this.username = comment.getUser().getUsername();
        this.cardTitle = comment.getCard().getTitles();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifedAt = comment.getModifiedAt();
    }
}
