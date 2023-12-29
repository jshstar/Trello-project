package com.nbc.trello.comment;

import com.nbc.trello.card.Repository.CardRepository;
import com.nbc.trello.card.entity.Card;
import com.nbc.trello.users.User;
import com.nbc.trello.users.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    public void createComment(UserDetailsImpl userDetails, Card card, CommentRequestDto commentRequestDto) {
        User user = userDetails.getUser();
        String content = commentRequestDto.getContent();
        Comment comment = new Comment(user, card, content);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(UserDetailsImpl userDetails, Long commentId, CommentRequestDto commentRequestDto) throws Exception {
        User user = userDetails.getUser();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new Exception("댓글이 존재하지 않습니다."));
        if(!user.equals(comment.getUser())){
            throw new Exception("작성자와 일치하지 않습니다.");
        }
        comment.update(commentRequestDto.getContent());
    }


    public void deleteComment(UserDetailsImpl userDetails, Long commentId) throws Exception {
        User user = userDetails.getUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new Exception("댓글이 존재하지 않습니다."));

        if(!user.equals(comment.getUser())){
            throw new Exception("작성자와 일치하지 않습니다.");
        }

        commentRepository.delete(comment);
    }

    public List<CommentResponseDto> getComment(Long cardId) throws Exception {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new Exception("카드가 존재하지 않습니다."));

        return commentRepository.findAllByCard(card)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

    }
}
