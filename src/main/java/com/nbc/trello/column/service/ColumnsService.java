package com.nbc.trello.column.service;


import com.nbc.trello.board.domain.Board;
import com.nbc.trello.board.repository.BoardRepository;
import com.nbc.trello.column.dto.request.ColumnsOrderRequestDto;
import com.nbc.trello.column.dto.request.ColumnsRequestDto;
import com.nbc.trello.column.dto.response.ColumnsResponseDto;
import com.nbc.trello.column.entity.Columns;
import com.nbc.trello.column.repository.ColumnsRepository;
import com.nbc.trello.global.exception.ApiException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.nbc.trello.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nbc.trello.global.exception.ErrorCode.COLUMNS_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class ColumnsService {

    private final ColumnsRepository columnsRepository;
    private final BoardRepository boardRepository;

    //컬럼 생성
    @Transactional
    public ColumnsResponseDto saveColumns(ColumnsRequestDto requestDto, Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));
        // 다음 가중치 구하기
        double nextWeight = 1;
        Optional<Columns> optionalColumn = columnsRepository.findFirstByBoardOrderByWeightDesc(board);
        if (optionalColumn.isPresent())
            nextWeight = optionalColumn.get().getWeight() + 1;

        Columns save = Columns.builder()
                .columnsName(requestDto.getColumnsName())
                .weight(nextWeight).board(board).build();
        columnsRepository.save(save);

        return ColumnsResponseDto.from(save);
    }

    //컬럼 전체 조회
    public List<ColumnsResponseDto> getColumnsList(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));
        List<Columns> columnList = columnsRepository.findAllByBoardOrderByWeightAsc(board);
        return columnList.stream().map(ColumnsResponseDto::from).toList();
    }

    //컬럼 이름 수정
    @Transactional
    public ColumnsResponseDto updateColumns(Long columnsId, ColumnsRequestDto requestDto) {
        Columns columns = findByColumnsId(columnsId);
        columns.updateColumns(requestDto.getColumnsName());

        return ColumnsResponseDto.from(columns);
    }

    //컬럼 순서 수정
    @Transactional
    public ColumnsResponseDto changeOrders(Long columnsId, Long boardId, ColumnsOrderRequestDto requestDto) {
        Columns columns = findByColumnsId(columnsId);

        Board board = boardRepository.findOneWithColumns(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_BOARD_ID));

        // 원하는 위치의 카드와 위치-1의 카드의 가중치 더해서 2로 나눈걸로 정렬 순서 설정
        List<Columns> columnList = board.getColumns();
        double weight = 0;
        Integer moveIndex = requestDto.getColumnsOrder();
        boolean moveColumnsUnderCheck
            = currentColumnsPositionCompareMovePosition(columns.getId(), moveIndex.longValue(), columnList);

        if (moveIndex == 0) {
            weight = columnList.get(0).getWeight() - 1;
        } else if (moveIndex >= columnList.size() -  1) {
            weight = columnList.get(columnList.size() - 1).getWeight() + 1;
        } else {
            if(moveColumnsUnderCheck) {
                weight = (columnList.get(moveIndex).getWeight() + columnList.get(moveIndex + 1).getWeight()) / 2;
            } else {
                weight = (columnList.get(moveIndex).getWeight()  + columnList.get(moveIndex - 1).getWeight()) / 2;
            }
        }
        columns.updateWeight(weight);

        return ColumnsResponseDto.from(columns);
    }

    // 옮기려는 칼럼의 위치가 현재 칼럼위치보다 큰경우
    public boolean currentColumnsPositionCompareMovePosition(Long columnsId, Long moveIndex, List<Columns> columnsList){
        boolean moveColumnsUnderCheck = false;
        for (int i = 0; i < columnsList.size(); i++) {
            if (Objects.equals(columnsId, columnsList.get(i).getId())) {
                if (i < moveIndex ) {
                    moveColumnsUnderCheck = true;
                    break;
                }
            }
        }
        return moveColumnsUnderCheck;

    }

    //컬럼삭제
    public void deleteColumns(Long columnsId) {
        Columns columns = findByColumnsId(columnsId);
        columnsRepository.delete(columns);
    }

    public Columns findByColumnsId(Long columnsId) {
        return columnsRepository.findById(columnsId)
                .orElseThrow(() -> new ApiException(COLUMNS_NOT_FOUND_EXCEPTION));
    }
}
