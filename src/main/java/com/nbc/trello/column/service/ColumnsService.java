package com.nbc.trello.column.service;


import static com.nbc.trello.global.exception.ErrorCode.BOARD_NOT_FOUND_EXCEPTION;
import static com.nbc.trello.global.exception.ErrorCode.COLUMNS_NOT_FOUND_EXCEPTION;

import com.nbc.trello.board.Board;
import com.nbc.trello.board.BoardRepository;
import com.nbc.trello.column.dto.ColumnsOrderRequestDto;
import com.nbc.trello.column.dto.ColumnsRequestDto;
import com.nbc.trello.column.dto.ColumnsResponseDto;
import com.nbc.trello.column.entity.Columns;
import com.nbc.trello.column.repository.ColumnsRepository;
import com.nbc.trello.global.exception.ApiException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColumnsService {

  ColumnsRepository columnsRepository;
  BoardRepository boardRepository;

  @Autowired
  public ColumnsService(ColumnsRepository columnsRepository,BoardRepository boardRepository){
    this.columnsRepository = columnsRepository;
    this.boardRepository = boardRepository;
  }

  //컬럼 생성
  @Transactional
  public ColumnsResponseDto saveColumns(ColumnsRequestDto requestDto,Long boardId) {
    Board board = boardRepository.findById(boardId).orElseThrow(()->new ApiException(BOARD_NOT_FOUND_EXCEPTION));
    Columns savedColumns = Columns.builder().columnsName(requestDto.getColumnsName()).columnsOrder(
        requestDto.getColumnsOrder()).board(board).build();
    columnsRepository.save(savedColumns);
    return ColumnsResponseDto.from(savedColumns);
  }

  //컬럼 전체 조회
  public List<ColumnsResponseDto> getColumnsList(Long boardId) {
    Board board = boardRepository.findById(boardId).orElseThrow(()->new ApiException(BOARD_NOT_FOUND_EXCEPTION));
    List<Columns> columnList = columnsRepository.findAllByBoardOrderByColumnOrder(board);
    return columnList.stream().map(ColumnsResponseDto::from).collect(Collectors.toList());
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
  public ColumnsResponseDto changeOrders(Long columnsId, ColumnsOrderRequestDto requestDto) {
    Columns columns = findByColumnsId(columnsId);
    columns.changeOrders(requestDto.getColumnsOrder());

    return ColumnsResponseDto.from(columns);
  }

  //컬럼삭제
  public void deleteColumns(Long columnsId) {
    Columns columns = findByColumnsId(columnsId);
    columnsRepository.delete(columns);
  }

  public Columns findByColumnsId(Long columnsId) {
    return columnsRepository.findById(columnsId).orElseThrow(()->new ApiException(COLUMNS_NOT_FOUND_EXCEPTION));//columnnotfoundexception
  }

//  public Long getAuthorIdByBoardId(Long boardId){
//      Board board = boardRepository.findById(boardId).orElseThrow(()->new ApiException(BOARD_NOT_FOUND_EXCEPTION)))//boardnotfoundexception
//    return board.getUser().getUserId;
//  }



}
