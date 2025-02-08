package com.dattp.productservice.controller.manager.response;

import com.dattp.productservice.base.response.dish.DishDetailBaseResponseResponse;
import com.dattp.productservice.entity.Dish;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishDetailManageResponse extends DishDetailBaseResponseResponse {
  public DishDetailManageResponse() {
    super();
  }

  public DishDetailManageResponse(Dish dish) {
    this.copyProperties(dish);
  }


  @Override
  public void copyProperties(Dish dish) {
    super.copyProperties(dish);
  }
}
