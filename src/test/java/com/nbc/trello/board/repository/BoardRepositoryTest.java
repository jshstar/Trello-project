package com.nbc.trello.board.repository;

import com.nbc.trello.TestDataFactory;
import com.nbc.trello.board.domain.Board;
import com.nbc.trello.column.entity.Columns;
import com.nbc.trello.config.TestQueryDslConfig;
import com.nbc.trello.global.exception.ApiException;
import com.nbc.trello.global.exception.ErrorCode;
import com.nbc.trello.users.entity.User;
import com.nbc.trello.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
@Slf4j
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void findAllByUserId() {
        // given
        User user = TestDataFactory.getUser();
        Long userId = user.getId();
        Board board = TestDataFactory.getBoard();
        Board board1 = TestDataFactory.getBoard();
        Board board2 = TestDataFactory.getBoard();
        User save = userRepository.save(user);
        save.addBoard(board);
        save.addBoard(board1);
        save.addBoard(board2);

        boardRepository.save(board);
        boardRepository.save(board1);
        boardRepository.save(board2);

        // when
        List<Board> boards = boardRepository.findAllByUserId(userId);

        // then
        assertThat(boards).hasSize(3);
    }

    @Test
    @Transactional
    void findOneWithColumns() {
        // given
        Board board = TestDataFactory.getBoard();
        Columns column = TestDataFactory.getColumn();
        Columns column1 = TestDataFactory.getColumn();
        Columns column2 = TestDataFactory.getColumn();
        board.addColumn(column);
        board.addColumn(column1);
        board.addColumn(column2);
        Board saved = boardRepository.save(board);
        Long boardId = saved.getId();
        // when
        Board findBoard = boardRepository.findOneWithColumns(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));
        List<Columns> columns = findBoard.getColumns();
        // then
        assertThat(columns).hasSize(3);
    }
}
