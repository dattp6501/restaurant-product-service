package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.pojo.DishOverview;
import com.dattp.productservice.service.RedisService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CartStorage extends Storage {
  @Autowired
  @Lazy
  private DishStorage dishStorage;

  public void addDishToCart(Long dishId) {
    String key = RedisKeyConfig.genKeyCartDish(jwtService.getUserId());
    List<Long> dishIds = redisService.getHashAll(key, Long.class);
    if (dishIds == null || dishIds.isEmpty()) {
      Map<Object, Object> data = new HashMap<>();
      data.put(dishId.toString(), dishId);
      redisService.putHashAll(key, data, RedisService.CacheTime.ONE_MONTH);
    } else {
      redisService.addElemntHash(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()), dishId.toString(), dishId, RedisService.CacheTime.ONE_MONTH);
    }
  }

  public void deleteDishInFromCart(Long dishId) {
    redisService.deleteHash(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()), dishId.toString());
  }

  public List<DishOverview> getListDishInCart(Long userId) {
    List<DishOverview> dishOverview = new ArrayList<>();
    List<Long> dishIds = redisService.getHashAll(RedisKeyConfig.genKeyCartDish(userId), Long.class);
    if (dishIds != null && !dishIds.isEmpty()) {
      dishIds.forEach(id -> {
        Dish dish = dishStorage.getDetailFromCacheAndDb(id);
        if (dish != null) {
          dishOverview.add(new DishOverview(dish));
        }
      });
    }
    return dishOverview;
  }
}