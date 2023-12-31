package com.nbc.trello.board.service;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.board.domain.BoardUser;
import com.nbc.trello.board.domain.BoardUserId;
import com.nbc.trello.board.repository.BoardRepository;
import com.nbc.trello.board.repository.BoardUserRepository;
import com.nbc.trello.board.dto.request.BoardCreateRequest;
import com.nbc.trello.board.dto.request.BoardInviteRequest;
import com.nbc.trello.board.dto.request.BoardUpdateRequest;
import com.nbc.trello.board.dto.response.BoardListResponse;
import com.nbc.trello.board.dto.response.BoardResponse;
import com.nbc.trello.column.dto.response.ColumnResponse;
import com.nbc.trello.column.entity.Columns;
import com.nbc.trello.column.repository.ColumnsRepository;
import com.nbc.trello.global.exception.ApiException;
import com.nbc.trello.global.exception.ErrorCode;
import com.nbc.trello.users.entity.User;
import com.nbc.trello.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardUserRepository boardUserRepository;
    private final UserRepository userRepository;
    private final ColumnsRepository columnsRepository;

    @Transactional
    public void createBoard(User user, BoardCreateRequest request) {
        Board board = new Board(request);
        board.setUser(user);

        boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(User user, Long boardId, BoardUpdateRequest request) {
        // 수정 권한 체크
        checkAuthorization(user, boardId);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));

        board.update(request);
    }

    @Transactional
    public void deleteBoard(User user, Long boardId) {
        // 삭제 권한 체크
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));
        if (!board.getUser().getId().equals(user.getId())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_BOARD);
        }

        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public List<BoardListResponse> findAll(User user) {
        List<Board> boards = boardRepository.findAllByUserId(user.getId());

        return boards.stream()
                .map(BoardListResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public BoardResponse findOne(User user, Long boardId) {
        // 조회 권한 체크
        checkAuthorization(user, boardId);

        // n+1을 1+1로 해결.. 근데 서버에서 루프 돌아야함
        Board board = boardRepository.findOneWithColumns(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));
        List<Long> columnIds = board.getColumns().stream().map(Columns::getId).toList();
        List<Columns> columnsList = columnsRepository.findAllByIdIn(columnIds);
        List<ColumnResponse> response = columnsList.stream().map(ColumnResponse::new).toList();

        return new BoardResponse(board, response);
    }

    @Transactional
    public void inviteUser(User user, Long boardId, BoardInviteRequest request) {
        // 초대 권한 체크
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));
        if (!board.getUser().getId().equals(user.getId())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_BOARD);
        }

        for (String username : request.getUsernames()) {
            User invitedUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApiException(ErrorCode.INVALID_USERNAME));
            // 중복 초대 체크
            if (boardUserRepository.existsById(new BoardUserId(invitedUser.getId(), boardId))) {
                throw new ApiException(ErrorCode.EXIST_BOARD_USER);
            }

            BoardUser boardUser = new BoardUser(new BoardUserId(user.getId(), boardId), user, board);
            boardUserRepository.save(boardUser);
        }
    }

    // 보드 유저인지 확인
    public void checkAuthorization(User user, Long boardId) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));
        Boolean exist = board.getUser().getId().equals(user.getId())
            || boardUserRepository.existsById(new BoardUserId(user.getId(), boardId));
        if (Boolean.FALSE.equals(exist) ) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_BOARD);
        }
    }
}
