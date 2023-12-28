package com.nbc.trello.column.controller;

import static com.nbc.trello.global.exception.ErrorCode.UNAUTHORIZATION_EXCEPTION;

import com.nbc.trello.column.dto.ColumnsOrderRequestDto;
import com.nbc.trello.column.dto.ColumnsRequestDto;
import com.nbc.trello.column.dto.ColumnsResponseDto;
import com.nbc.trello.column.service.ColumnsService;
import com.nbc.trello.global.dto.ApiResponse;
import com.nbc.trello.global.exception.ApiException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class ColumnsController {

  private final ColumnsService columnsService;

  // 컬럼 생성
  @PostMapping("/{boardId}/columns")
  public ResponseEntity<ApiResponse<Void>> saveColumns(
      @Valid @RequestBody ColumnsRequestDto requestDto,
//      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long boardId ) {

//    User loginUser = userDetails.getUser();

    columnsService.saveColumns(requestDto, boardId);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.of(HttpStatus.CREATED.value(),"column 생성 성공",null));
  }

   // 컬럼 단건 조회
//  @GetMapping("/{boardId}/columns/{columnIds}")
//  public ResponseEntity<ApiResponse<ColumnResponseDto>> getcolumns(@PathVariable Long columnsId) {
//    ColumnsResponseDto columnsResponseDto = ColumnsResponseDto.from(
//        columnsService.findByColumnsId(columnsId));
//    return ResponseEntity.status(HttpStatus.OK)
//        .body(ApiResponse.of(HttpStatus.OK.value(),"컬럼 단건조회 성공",columnResponseDto));
//  }

 //컬럼 목록조회
  @GetMapping("/{boardId}/columns")
  public ResponseEntity<ApiResponse<List<ColumnsResponseDto>>> getColumnsList(@PathVariable Long boardId) {
    List<ColumnsResponseDto> columnsList = columnsService.getColumnsList(boardId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.of(HttpStatus.OK.value(),"Column 목록 조회 성공",columnsList));
  }

  // 컬럼 이름 수정
  @PatchMapping("/{boardId}/columns/{columnsId}")
  public ResponseEntity<ApiResponse<ColumnsResponseDto>> updateColumns(
      @PathVariable Long boardId,
      @PathVariable Long columnsId,
      @Valid @RequestBody ColumnsRequestDto requestDto
//      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {

//    User loginuser = userDetails.getUser();
//    if (!haveModifyAuthorization(loginuser, columnId)) {
//      throw new ApiException(UNAUTHORIZATION_EXCEPTION);
//    }

    ColumnsResponseDto responseDto = columnsService.updateColumns(columnsId, requestDto);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.of(HttpStatus.OK.value(), "Column 수정 성공",responseDto));
  }

//컬럼 순서 수정
    @PatchMapping("/{boardId}/columns/{columnsId}/orders")
  public ResponseEntity<ApiResponse<ColumnsResponseDto>> changeOrders(@PathVariable Long boardId,@PathVariable Long columnsId,@RequestBody ColumnsOrderRequestDto requestDto){
    ColumnsResponseDto responseDto = columnsService.changeOrders(columnsId, requestDto);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.of(HttpStatus.OK.value(),"컬럼 순서 수정 성공",responseDto));
  }

  // 컬럼 삭제
  @DeleteMapping("/{boardId}/columns/{columnsId}")
  public ResponseEntity<ApiResponse<Void>> deleteColumns(@PathVariable Long boardId,
      @PathVariable Long columnsId
//      @AuthenticationPrincipal UserDetailsImpl userDetails
      ) {

//    User loginuser = userDetails.getUser();
//    if (!haveModifyAuthorization(loginuser, columnId)) {
//      throw new ApiException(UNAUTHORIZATION_EXCEPTION);
//    }

    columnsService.deleteColumns(columnsId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.of(HttpStatus.NO_CONTENT.value(), "Column 삭제 완료",null));
  }



//  public boolean haveModifyAuthorization(User loginUser, Long boardId) {
//    Long authorId = columnService.getAuthorIdByBoardId(boardId);
//    return loginUser.getUserId().equals(authorId);
//  }
}
