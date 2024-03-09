package com.dattp.productservice.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
@Getter
public class DishRequestDTO {
    private Long id;
    private int state;

    @NotNull(message = "Thiếu trường dữ liệu name(tên món ăn) khi gửi đi")
    @NotEmpty(message = "Trường name(tên món ăn) không được để trống")
    private String name;

    @Min(value = 1, message = "Trường dữ liệu price(giá món ắn) phải lớn hơn 0")
    private float price;

    private String description;
}