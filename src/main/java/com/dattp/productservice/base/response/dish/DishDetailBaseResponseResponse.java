package com.dattp.productservice.base.response.dish;

import com.dattp.productservice.controller.user.response.CommentDishResponse;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.state.DishState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Setter
@Getter
public class DishDetailBaseResponseResponse extends DishOverviewResponse {
  private DishState state;

  private List<CommentDishResponse> comments;

  public DishDetailBaseResponseResponse() {
    super();
  }

  public DishDetailBaseResponseResponse(Dish dish) {
    copyProperties(dish);
  }

  @Override
  public void copyProperties(Dish dish) {
    super.copyProperties(dish);
    BeanUtils.copyProperties(dish, this);
    super.setCreateAt(dish.getCreateAt());
    super.setUpdateAt(dish.getUpdateAt());
  }
}
