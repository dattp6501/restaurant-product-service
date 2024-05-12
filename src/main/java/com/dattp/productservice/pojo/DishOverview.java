package com.dattp.productservice.pojo;

import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.utils.JSONUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Getter
@Setter
public class DishOverview implements Serializable {
  private Long id;
  private String name;
  private Float price;
  private String image;
  private String description;

  public DishOverview(){
  }

  public DishOverview(Dish dish){
    copyProperties(dish);
  }

  public void copyProperties(Dish dish){
    BeanUtils.copyProperties(dish, this);
  }

  public static Dish toDish(DishOverview dishOverview){
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishOverview, dish);
    return dish;
  }

  @Override
  public String toString() {
    return JSONUtils.toJson(this);
  }
}