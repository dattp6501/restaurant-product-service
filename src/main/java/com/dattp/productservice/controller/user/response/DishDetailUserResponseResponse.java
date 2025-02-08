package com.dattp.productservice.controller.user.response;

import com.dattp.productservice.base.response.dish.DishDetailBaseResponseResponse;
import com.dattp.productservice.entity.Dish;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class DishDetailUserResponseResponse extends DishDetailBaseResponseResponse {
  public DishDetailUserResponseResponse() {
//    super();
  }

  public static DishDetailUserResponseResponse gen(Dish dish) {
    DishDetailUserResponseResponse resp = new DishDetailUserResponseResponse();
    resp.copyProperties(dish);
    return resp;
  }


  @Override
  public void copyProperties(Dish dish) {
    BeanUtils.copyProperties(dish, this);
    super.copyProperties(dish);
  }
}