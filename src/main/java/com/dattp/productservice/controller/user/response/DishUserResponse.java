package com.dattp.productservice.controller.user.response;

import com.dattp.productservice.base.response.dish.DishBaseResponse;
import com.dattp.productservice.entity.Dish;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishUserResponse extends DishBaseResponse {
  public DishUserResponse() {
    super();
  }

  public DishUserResponse(Dish dish) {
    this.copyProperties(dish);
  }


  public void copyProperties(Dish dish) {
    super.copyProperties(dish);
  }
}