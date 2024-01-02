package com.nbc.trello.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbc.trello.card.entity.Card;
import com.nbc.trello.card.service.CardService;
import com.nbc.trello.comment.repository.CommentRepository;
import com.nbc.trello.comment.dto.request.CommentRequestDto;
import com.nbc.trello.comment.dto.response.CommentResponseDto;
import com.nbc.trello.comment.entity.Comment;
import com.nbc.trello.global.exception.ApiException;
import com.nbc.trello.global.exception.ErrorCode;
import com.nbc.trello.users.entity.User;
import com.nbc.trello.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final CardService cardService;

    public CommentResponseDto createComment(UserDetailsImpl userDetails, Long cardId, CommentRequestDto commentRequestDto) {
        User user = userDetails.getUser();
        Card card = cardService.findCard(cardId);
        String content = commentRequestDto.getContent();
        Comment comment = new Comment(user, card, content);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);

    }

    @Transactional
    public CommentResponseDto updateComment(UserDetailsImpl userDetails, Long commentId, CommentRequestDto commentRequestDto) throws Exception {
        User user = userDetails.getUser();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ApiException(ErrorCode.NOT_EXIST_COMMENT));
        if (!user.getUsername().equals(comment.getUser().getUsername())) {
            throw new ApiException(ErrorCode.NOT_EQUAL_CREATE_USER);
        }
        comment.update(commentRequestDto.getContent());
        return new CommentResponseDto(comment);

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
