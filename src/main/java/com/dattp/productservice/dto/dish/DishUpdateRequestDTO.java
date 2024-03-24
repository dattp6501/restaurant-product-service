package com.dattp.productservice.dto.dish;

import com.dattp.productservice.entity.state.DishState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DishUpdateRequestDTO {
  @NotNull(message = "Thiếu trường id")
  @Min(value = 1, message = "Thiếu trường id")
  private Long id;

  @NotNull(message = "Thiếu trường state(trạng thái món ăn)")
  private DishState state;

  @NotNull(message = "Thiếu trường dữ liệu name(tên món ăn) khi gửi đi")
  @NotEmpty(message = "Trường name(tên món ăn) không được để trống")
  private String name;

  @Min(value = 1, message = "Trường dữ liệu price(giá món ắn) phải lớn hơn 0")
  private float price;

  private String image;

  private String description;
}
