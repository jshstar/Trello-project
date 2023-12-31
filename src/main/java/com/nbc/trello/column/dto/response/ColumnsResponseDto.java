package com.nbc.trello.column.dto.response;

import com.nbc.trello.column.entity.Columns;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ColumnsResponseDto {

  private Long columnsId;
  private String columnsName;
  private Long boardId;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;


  public static ColumnsResponseDto from(Columns columns) {
    return new ColumnsResponseDto(
            columns.getId(),
            columns.getColumnsName(),
            columns.getBoard().getId(),
            columns.getCreatedAt(),
            columns.getModifiedAt()
    );
  }
}
