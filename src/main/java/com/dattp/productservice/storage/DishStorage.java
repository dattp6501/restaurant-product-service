package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.pojo.DishOverview;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DishStorage extends Storage{
  public List<Dish> findListFromCacheAndDB(Pageable pageable){
    String keyCache = RedisKeyConfig.genKeyAllDishOverview();
    Map<Object, Object> dishMap = null;
    if(redisService.hasKey(keyCache)){
      dishMap = redisService.getHashAll(keyCache);
    }else{
      dishMap = new HashMap<>();
      List<Dish> dishs = dishRepository.findAllByStateIn(List.of(DishState.ACTIVE));
      for(Dish d : dishs){
        DishOverview dishOverview = new DishOverview(d);
        dishMap.put(d.getId().toString(), dishOverview);
      }
      redisService.putHashAll(keyCache, dishMap, -1);
    }
    //
    return dishMap.values().stream()
      .map(o -> DishOverview.toDish((DishOverview) o))
      .collect(Collectors.toList())
      .subList(pageable.getPageSize()*pageable.getPageNumber(), pageable.getPageSize()*pageable.getPageNumber()+pageable.getPageSize());
  }

  public Dish getDetailFromCacheAndDb(Long id){
    Dish dish = null;
    String hashKey = RedisKeyConfig.genKeyDish(id);
    if(redisService.hasKey(hashKey))
      dish = (Dish) redisService.getHash(id.toString(), hashKey);
    else{
      dish = dishRepository.findById(id).orElseThrow();
      redisService.putHash(dish.getId().toString(), hashKey, dish, -1);
    }
    return dish;
  }
}