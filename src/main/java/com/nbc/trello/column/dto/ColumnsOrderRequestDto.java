package com.nbc.trello.column.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ColumnsOrderRequestDto {
  @NotBlank
  Integer columnsOrder;
}
