package com.dattp.productservice.service;

import com.dattp.productservice.base.response.dish.DishOverviewResponse;
import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.controller.user.dto.DishInCartRequestDTO;
import com.dattp.productservice.utils.JSONUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class CartService extends com.dattp.productservice.service.Service {
  @Autowired
  @Lazy
  private DishService dishService;

  public void addDishToCart(DishInCartRequestDTO dto) {
    DishOverviewResponse dish = dishService.getDetail(dto.getDishId());
    //lay thong tin gio hang cua user
    String key = RedisKeyConfig.genKeyCartDish(jwtService.getUserId());
    List<Long> dishIds = redisService.getHashAll(key, Long.class);
    //neu chua co thong tin gio hang => tao
    if (dishIds == null || dishIds.isEmpty()) {
      Map<Object, Object> data = new HashMap<>();
      data.put(dto.getDishId().toString(), JSONUtils.toJson(dish));
      redisService.putHashAll(key, data, RedisService.CacheTime.ONE_MONTH);
    } else {
      redisService.addElemntHash(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()),
          dto.getDishId().toString(), JSONUtils.toJson(dish), RedisService.CacheTime.ONE_MONTH);
    }
  }

  public void deleteDish(Long dishId) {
    redisService.deleteHash(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()), dishId.toString());
  }

  public List<DishOverviewResponse> getListDish() {
    return redisService.getHashAll(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()), DishOverviewResponse.class);
  }
}