package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.exception.BadRequestException;
import com.dattp.productservice.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Log4j2
public class DishStorage extends Storage {
  //===================================== LIST DISH ==========================================
  /*
   * user
   * */

  /*
   * admin
   * */

  //======================================= DISH DETAIL ===========================
  /*
   * user
   * */
  public Dish getDetailFromCacheAndDb(Long id) {
    Dish dish = null;
    String key = RedisKeyConfig.genKeyDish(id);
    if (cacheEnable) {
      dish = redisService.getEntity(key, Dish.class);
      log.debug("======> getDetailFromCacheAndDb::dish::cache::{}", dish);
    }

    if (dish == null) {
      dish = dishRepository.findById(id).orElseThrow(() -> new BadRequestException(String.format("dish(id=%d) not found", id)));
    }

    if (cacheEnable) {
      redisService.setEntity(key, dish, RedisService.CacheTime.ONE_DAY);
    }

    return dish;
  }


  /*
   * admin
   * */
  public Dish getDetailDishFromDB(Long id) {
    return dishRepository.findById(id).orElseThrow(() -> new BadRequestException(String.format("dish(id=%d) not found", id)));
  }

  //======================================= SAVE DISH ============================
  /*
   * admin
   * */

  /*
   * save dish
   * */

  //==============================================   COMMENT ========================================

  public List<String> genCacheKeys(Dish dish) {
    List<String> keys = new ArrayList<>();
    for (int i = 0; i < 10; i++)
      keys.add(RedisKeyConfig.genKeyPageDishOverview(i));
    if (Objects.nonNull(dish)) keys.add(RedisKeyConfig.genKeyDish(dish.getId()));
    return keys;
  }
}