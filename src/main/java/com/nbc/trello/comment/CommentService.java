package com.nbc.trello.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public void createComment(User user, Card card, CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(user, card, commentRequestDto);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(User user, Long commentId, CommentRequestDto commentRequestDto) throws Exception {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new Exception("댓글이 존재하지 않습니다."));

        Comment.update(commentRequestDto);
    }


    public void deleteComment(User user, Long commentId) throws Exception {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new Exception("댓글이 존재하지 않습니다."));

        commentRepository.delete(comment);
    }
}
