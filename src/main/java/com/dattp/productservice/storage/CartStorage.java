package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.pojo.DishOverview;
import com.dattp.productservice.service.RedisService;
import org.springframework.stereotype.Component;

@Component
public class CartStorage extends Storage{
  public void addDishToCart(Dish dish){
    redisService.addElemntHash(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()), dish.getId().toString(), new DishOverview(dish), RedisService.CacheTime.ONE_MONTH);
  }

  public void deleteDishInFromCart(Dish dish){
    redisService.deleteHash(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()), dish.getId().toString());
  }
}