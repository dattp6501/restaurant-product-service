package com.dattp.productservice.service;

import com.dattp.productservice.dto.dish.DishInCartRequestDTO;
import com.dattp.productservice.entity.Dish;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CartService extends com.dattp.productservice.service.Service {
  public void addDishToCart(DishInCartRequestDTO dto){
    Dish dish = dishStorage.getDetailFromCacheAndDb(dto.getDishId());
    //add to cart
    cartStorage.addDishToCart(dish);
  }
}