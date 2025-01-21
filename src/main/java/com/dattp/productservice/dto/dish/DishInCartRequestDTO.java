package com.dattp.productservice.dto.dish;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DishInCartRequestDTO {
  @NotNull(message = "(dishId) Khong duoc de trong")
  private Long dishId;
}