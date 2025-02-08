package com.dattp.productservice.base.response.dish;

import com.dattp.productservice.base.response.ProductBaseResponse;
import com.dattp.productservice.controller.user.response.CommentDishResponseDTO;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.state.DishState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Setter
@Getter
public class DishBaseResponse extends ProductBaseResponse {
  private DishState state;

  private List<CommentDishResponseDTO> comments;

  public DishBaseResponse() {
    super();
  }

  public DishBaseResponse(Dish dish) {
    copyProperties(dish);
  }

  public void copyProperties(Dish dish) {
    BeanUtils.copyProperties(dish, this);
    super.setCreateAt(dish.getCreateAt());
    super.setUpdateAt(dish.getUpdateAt());
  }
}
