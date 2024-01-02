package com.nbc.trello.column.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ColumnsRequestDto {

  @NotBlank
  @Size(max = 200)
  String columnsName;
}
