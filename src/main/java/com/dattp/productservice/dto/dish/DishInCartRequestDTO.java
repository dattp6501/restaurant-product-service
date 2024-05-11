package com.dattp.productservice.dto.dish;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishInCartRequestDTO {
  @NotNull(message = "(dishId) Khong duoc de trong")
  private Long dishId;
}