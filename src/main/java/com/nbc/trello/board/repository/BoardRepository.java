package com.nbc.trello.board.repository;

import com.nbc.trello.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Boolean existsBoardByIdAndUserId(Long boardId, Long userId);
}
