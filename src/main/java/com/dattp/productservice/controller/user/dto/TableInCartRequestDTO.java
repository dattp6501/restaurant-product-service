package com.dattp.productservice.controller.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TableInCartRequestDTO {
  @NotNull(message = "(tableId) Khong duoc de trong")
  private Long tableId;
}
