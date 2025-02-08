package com.dattp.productservice.base.response.dish;

import com.dattp.productservice.base.response.ProductBaseResponse;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.utils.JSONUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DishOverviewResponse extends ProductBaseResponse implements Serializable {
  public DishOverviewResponse() {
    super();
  }

  public DishOverviewResponse(Dish dish) {
    copyProperties(dish);
  }

  public void copyProperties(Dish dish) {
    BeanUtils.copyProperties(dish, this);
    super.setCreateAtLong(dish.getCreateAt());
    super.setUpdateAtLong(dish.getUpdateAt());
  }

  @Override
  public String toString() {
    return JSONUtils.toJson(this);
  }
}