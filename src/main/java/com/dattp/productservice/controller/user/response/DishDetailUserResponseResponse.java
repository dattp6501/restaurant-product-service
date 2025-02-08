package com.dattp.productservice.controller.user.response;

import com.dattp.productservice.base.response.dish.DishDetailBaseResponseResponse;
import com.dattp.productservice.entity.Dish;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishDetailUserResponseResponse extends DishDetailBaseResponseResponse {
  public DishDetailUserResponseResponse() {
    super();
  }

  public DishDetailUserResponseResponse(Dish dish) {
    this.copyProperties(dish);
  }


  @Override
  public void copyProperties(Dish dish) {
    super.copyProperties(dish);
  }
}