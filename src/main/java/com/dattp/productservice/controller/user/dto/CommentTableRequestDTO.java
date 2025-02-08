package com.dattp.productservice.controller.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class CommentTableRequestDTO {
  private Long id;

  @Min(value = 1, message = "Số sao(star) phải lớn hơn 0")
  private int star;

  private String comment;

  @NotNull(message = "ID của bàn(table_id) cần đánh giá không được để trống")
  @JsonProperty("table_id")
  private Long tableId;
}