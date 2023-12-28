package com.nbc.trello.column.dto;

import com.nbc.trello.column.entity.Columns;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ColumnsResponseDto {

  Long columnsId;
  String columnsName;
  Integer columnsOrder;
  Long boardId;
  LocalDateTime createdAt;
  LocalDateTime modifiedAt;


  public static ColumnsResponseDto from(Columns columns) {
    return new ColumnsResponseDto(
        columns.getColumnsId(),
        columns.getColumnsName(),
        columns.getColumnsOrder(),
        columns.getBoard().getBoardId(),
        columns.getCreatedAt(),
        columns.getModifiedAt()
    );
  }
}