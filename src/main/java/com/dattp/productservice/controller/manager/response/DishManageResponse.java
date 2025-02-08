package com.dattp.productservice.controller.manager.response;

import com.dattp.productservice.base.response.dish.DishBaseResponse;
import com.dattp.productservice.entity.Dish;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishManageResponse extends DishBaseResponse {
  public DishManageResponse() {
    super();
  }

  public DishManageResponse(Dish dish) {
    this.copyProperties(dish);
  }


  public void copyProperties(Dish dish) {
    super.copyProperties(dish);
  }
}
