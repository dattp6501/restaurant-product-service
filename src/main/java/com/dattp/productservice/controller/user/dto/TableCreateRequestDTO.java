package com.dattp.productservice.controller.user.dto;

import com.dattp.productservice.entity.state.TableState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class TableCreateRequestDTO {
  private TableState state;

  @NotNull(message = "Thiếu trường dữ liệu name khi gửi đi")
  @NotEmpty(message = "Tên bàn không được để trống")
  private String name;

  private String image;

  @Min(value = 1, message = "Số người có thể chứa phải lớn hơn hoặc bằng 0")
  private int amountOfPeople;

  @Min(value = 1, message = "Giá thuê phải lớn hơn hoặc bằng 0")
  private float price;

  @JsonFormat(pattern = "HH:mm")
  private Date from;

  @JsonFormat(pattern = "HH:mm")
  private Date to;

  private String description;
}