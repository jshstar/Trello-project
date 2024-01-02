package com.nbc.trello.column.controller;

import com.nbc.trello.board.service.BoardService;
import com.nbc.trello.column.dto.request.ColumnsOrderRequestDto;
import com.nbc.trello.column.dto.request.ColumnsRequestDto;
import com.nbc.trello.column.dto.response.ColumnsResponseDto;
import com.nbc.trello.column.service.ColumnsService;
import com.nbc.trello.global.response.ApiResponse;
import com.nbc.trello.security.UserDetailsImpl;
import jakarta.validation.Valid;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class ColumnsController {

    private final ColumnsService columnsService;
    private final BoardService boardService;

    // 컬럼 생성
    @PostMapping("/{boardId}/columns")
    public ResponseEntity<ApiResponse<Void>> saveColumns(
            @Valid @RequestBody ColumnsRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId) {
        boardService.checkAuthorization(userDetails.getUser(), boardId);
        columnsService.saveColumns(requestDto, boardId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "column 생성 성공", null));
    }

    //컬럼 목록조회
    @GetMapping("/{boardId}/columns")
    public ResponseEntity<ApiResponse<List<ColumnsResponseDto>>> getColumnsList(@PathVariable Long boardId) {
        List<ColumnsResponseDto> columnsList = columnsService.getColumnsList(boardId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "Column 목록 조회 성공", columnsList));
    }

    // 컬럼 이름 수정
    @PutMapping("/{boardId}/columns/{columnsId}")
    public ResponseEntity<ApiResponse<ColumnsResponseDto>> updateColumns(
            @PathVariable Long boardId,
            @PathVariable Long columnsId,
            @Valid @RequestBody ColumnsRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.checkAuthorization(userDetails.getUser(), boardId);

        ColumnsResponseDto responseDto = columnsService.updateColumns(columnsId, requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "Column 수정 성공", responseDto));
    }

    //컬럼 순서 수정
    @PostMapping("/{boardId}/columns/{columnsId}/orders")
    public ResponseEntity<ApiResponse<ColumnsResponseDto>> changeOrders(
            @PathVariable Long boardId,
            @PathVariable Long columnsId,
            @RequestBody ColumnsOrderRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.checkAuthorization(userDetails.getUser(), boardId);

        ColumnsResponseDto responseDto = columnsService.changeOrders(columnsId, boardId, requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "컬럼 순서 수정 성공", responseDto));
    }

    // 컬럼 삭제
    @DeleteMapping("/{boardId}/columns/{columnsId}")
    public ResponseEntity<ApiResponse<Void>> deleteColumns(
            @PathVariable Long boardId,
            @PathVariable Long columnsId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.checkAuthorization(userDetails.getUser(), boardId);

        columnsService.deleteColumns(columnsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.of(HttpStatus.NO_CONTENT.value(), "Column 삭제 완료", null));
    }
}
