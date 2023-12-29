package com.nbc.trello.board.repository;

import com.nbc.trello.board.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    List<Board> findAllByUserId(Long userId);

    Optional<Board> findOneWithColumns(Long boardId);
}
