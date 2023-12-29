package com.nbc.trello.board.repository;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.board.domain.QBoard;
import com.nbc.trello.column.QColumns;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.nbc.trello.board.domain.QBoard.board;
import static com.nbc.trello.column.QColumns.columns;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> findAllByUserId(Long userId) {
        return queryFactory.selectFrom(board)
                .where(board.user.id.eq(userId))
                .fetch();
    }

    @Override
    public Optional<Board> findOneWithColumns(Long boardId) {
        return Optional.ofNullable(queryFactory.selectFrom(board)
                .leftJoin(board.columns, columns).fetchJoin()
                .where(board.id.eq(boardId))
                .fetchOne());
    }
}
