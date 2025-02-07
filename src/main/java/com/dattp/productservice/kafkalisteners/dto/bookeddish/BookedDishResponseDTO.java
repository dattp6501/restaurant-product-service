package com.dattp.productservice.kafkalisteners.dto.bookeddish;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class BookedDishResponseDTO {
  private Long id;
  private Long dishId;
  private String name;
  private Integer total;
  private Float price;
  private BookedDishState state;

  public BookedDishResponseDTO() {
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BookedDishResponseDTO other)) return false;
    return Objects.equals(id, other.id);
  }
}