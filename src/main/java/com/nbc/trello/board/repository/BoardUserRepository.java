package com.nbc.trello.board.repository;

import com.nbc.trello.board.domain.BoardUser;
import com.nbc.trello.board.domain.BoardUserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardUserRepository extends JpaRepository<BoardUser, BoardUserId> {
}
