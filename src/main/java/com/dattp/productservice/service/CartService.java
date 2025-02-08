package com.dattp.productservice.service;

import com.dattp.productservice.controller.user.dto.DishInCartRequestDTO;
import com.dattp.productservice.base.response.dish.DishOverviewResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class CartService extends com.dattp.productservice.service.Service {
  public void addDishToCart(DishInCartRequestDTO dto) {
    cartStorage.addDishToCart(dto.getDishId());
  }

  public void deleteDish(Long dishId) {
    cartStorage.deleteDishInFromCart(dishId);
  }

  public List<DishOverviewResponse> getListDish() {
    return cartStorage.getListDishInCart(jwtService.getUserId());
  }
}