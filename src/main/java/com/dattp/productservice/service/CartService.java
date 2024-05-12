package com.dattp.productservice.service;

import com.dattp.productservice.dto.dish.DishInCartRequestDTO;
import com.dattp.productservice.dto.table.TableInCartRequestDTO;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.pojo.DishOverview;

import com.dattp.productservice.pojo.TableOverview;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CartService extends com.dattp.productservice.service.Service {
  public void addDishToCart(DishInCartRequestDTO dto){
    Dish dish = dishStorage.getDetailFromCacheAndDb(dto.getDishId());
    //add to cart
    cartStorage.addDishToCart(dish);
  }
  public List<DishOverview> getListDish(){
    return cartStorage.getListDishInCart(jwtService.getUserId());
  }

  public void addTableToCart(TableInCartRequestDTO dto){
    TableE table = tableStorage.getDetailFromCacheAndDB(dto.getTableId());
    //add to cart
    cartStorage.addTableToCart(table);
  }
  public List<TableOverview> getListTable(){
    return cartStorage.getListTableInCart(jwtService.getUserId());
  }
}