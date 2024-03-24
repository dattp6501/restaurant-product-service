package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.pojo.DishOverview;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DishStorage extends Storage{
  //===================================== DISH OVERVIEW ==========================================
  /*
   * get list dish
   * */
  public List<Dish> findListFromCacheAndDB(Pageable pageable){
    String keyCache = RedisKeyConfig.genKeyAllDishOverview();
    Map<Object, Object> dishMap = null;
    if(redisService.hasKey(keyCache)) dishMap = redisService.getHashAll(keyCache);
    else dishMap = initDishOverviewCache();

    //
    return dishMap.values().stream()
      .map(o -> DishOverview.toDish((DishOverview) o))
      .collect(Collectors.toList());
  }
  /*
  * init list dish cache
  * */
  public Map<Object, Object> initDishOverviewCache(){
    Map<Object, Object> dishMap = new HashMap<>();
    try{
      List<Dish> dishs = dishRepository.findAllByStateIn(List.of(DishState.ACTIVE));
      for(Dish d : dishs){
        DishOverview dishOverview = new DishOverview(d);
        dishMap.put(d.getId().toString(), dishOverview);
      }
      redisService.putHashAll(RedisKeyConfig.genKeyAllDishOverview(), dishMap, null);
    }catch (Exception e){
      e.printStackTrace();
    }
    return dishMap;
  }

  public void addOverviewDishToCache(Dish dish){
    try {
      //neu key luu ton tai => them
      if(redisService.hasKey(RedisKeyConfig.genKeyAllDishOverview())){
        redisService.addElemntHash(dish.getId().toString(), RedisKeyConfig.genKeyAllDishOverview(), new DishOverview(dish));
        return;
      }
      //khoi tao lai danh sach
      initDishOverviewCache();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void updateOverviewDishFromCache(Dish dish){
    try {
      //neu key luu ton tai => cap nhat lai gia tri
      if(redisService.hasKey(RedisKeyConfig.genKeyAllDishOverview())){
        redisService.updateHash(dish.getId().toString(), RedisKeyConfig.genKeyAllDishOverview(), new DishOverview(dish));
        return;
      }
      //khoi tao lai danh sach
      initDishOverviewCache();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  //=========================================   DISH DETAIL  =========================================
  /*
   * create dish
   * */
  public Dish addToCacheAndDb(Dish dish){
    dish = dishRepository.save(dish);
    // cache
    try{
      //detail
      addToCache(dish);
      //overview
      if(redisService.hasKey(RedisKeyConfig.genKeyAllDishOverview())) addOverviewDishToCache(dish);
      else initDishOverviewCache();
    }catch (Exception e){
      e.printStackTrace();
    }
    return dish;
  }
  public void addToCache(Dish dish){
    try{
      //detail
      redisService.createHash(dish.getId().toString(), RedisKeyConfig.genKeyDish(dish.getId()), dish, null);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  /*
   * update dish
   * */
  public Dish updateFromCacheAndDb(Dish dish){
    dish = dishRepository.save(dish);
    // cache
    try{
      //detail
      redisService.updateHash(dish.getId().toString(), RedisKeyConfig.genKeyDish(dish.getId()), dish);
      //overview
      updateOverviewDishFromCache(dish);
    }catch (Exception e){
      e.printStackTrace();
    }
    return dish;
  }
  /*
  * get detail dish
  * */
  public Dish getDetailFromCacheAndDb(Long id){
    Dish dish = null;
    String hashKey = RedisKeyConfig.genKeyDish(id);
    if(redisService.hasKey(hashKey)) dish = (Dish) redisService.getHash(id.toString(), hashKey);
    else{
      dish = dishRepository.findById(id).orElseThrow();
      addToCache(dish);
    }
    return dish;
  }

  //==============================================   COMMENT ========================================
  public boolean addCommentDish(CommentDish comment){
    Long dishId = comment.getDish().getId();
    boolean ok = false;
    if(commentDishRepository.findByDishIdAndUserId(dishId, comment.getUser().getId()) != null)
      ok = commentDishRepository.update(comment.getStar(), comment.getComment(), dishId, comment.getUser().getId(), comment.getDate())>0;
    else
      ok = commentDishRepository.save(comment.getStar(), comment.getComment(), dishId, comment.getUser().getId(), comment.getUser().getUsername(), comment.getDate())>=1;
    //cache
    String hashKey = RedisKeyConfig.genKeyCommentDish(dishId);
    if(!redisService.hasKey(hashKey)) initCommentDishCache(dishId);
    redisService.addElemntHash(comment.getUser().getId().toString(), hashKey, comment);
    return ok;
  }

  public void initCommentDishCache(Long dishId){
    // userId, comment
    Map<Object,Object> map = new HashMap<>();
    try {
      List<CommentDish> comments = dishRepository.findById(dishId).orElseThrow().getCommentDishs();
      if(comments==null) comments = new ArrayList<>();
      comments.forEach(c->{
        map.put(c.getUser().getId().toString(), c);
      });
      redisService.putHashAll(RedisKeyConfig.genKeyCommentDish(dishId), map, null);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}