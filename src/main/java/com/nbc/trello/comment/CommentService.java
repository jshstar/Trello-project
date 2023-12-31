package com.nbc.trello.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbc.trello.card.entity.Card;
import com.nbc.trello.card.service.CardService;
import com.nbc.trello.global.exception.ApiException;
import com.nbc.trello.global.exception.ErrorCode;
import com.nbc.trello.users.User;
import com.nbc.trello.users.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final CardService cardService;

    public void createComment(UserDetailsImpl userDetails, Long cardId, CommentRequestDto commentRequestDto) {
        User user = userDetails.getUser();
        Card card = cardService.findCard(cardId);
        String content = commentRequestDto.getContent();
        Comment comment = new Comment(user, card, content);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(UserDetailsImpl userDetails, Long commentId, CommentRequestDto commentRequestDto) throws Exception {
        User user = userDetails.getUser();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ApiException(ErrorCode.NOT_EXIST_COMMENT));
        if (!user.getUsername().equals(comment.getUser().getUsername())) {
            throw new ApiException(ErrorCode.NOT_EQUAL_CREATE_USER);
        }
        comment.update(commentRequestDto.getContent());

    }


    public void deleteComment(UserDetailsImpl userDetails, Long commentId) throws Exception {
        User user = userDetails.getUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ApiException(ErrorCode.NOT_EXIST_COMMENT));

        if(!user.getUsername().equals(comment.getUser().getUsername())){
            throw new ApiException(ErrorCode.NOT_EQUAL_CREATE_USER);
        }

        commentRepository.delete(comment);
    }

    public List<CommentResponseDto> getComment(Long cardId) throws Exception {
        return commentRepository.findAllByCardId(cardId)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
