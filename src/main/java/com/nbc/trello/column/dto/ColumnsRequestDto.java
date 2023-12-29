package com.nbc.trello.column.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ColumnsRequestDto {

  @NotBlank
  @Size(max = 200)
  String columnsName;

  Integer columnsOrder;

}