package com.nbc.trello.column.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ColumnsOrderRequestDto {
  @NotBlank
  Integer columnsOrder;
}
