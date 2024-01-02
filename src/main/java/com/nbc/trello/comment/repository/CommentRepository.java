package com.nbc.trello.comment.repository;

import com.nbc.trello.comment.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCardId(Long cardId);
}
