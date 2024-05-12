package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.exception.BadRequestException;
import com.dattp.productservice.pojo.DishOverview;
import com.dattp.productservice.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Log4j2
public class DishStorage extends Storage{
  //===================================== LIST DISH ==========================================
  /*
   * user
   * */
  public List<DishOverview> findListFromCacheAndDB(Pageable pageable){
    String key = RedisKeyConfig.genKeyPageDishOverview(pageable.getPageNumber());
    List<DishOverview> dishs = redisService.getHashAll(key, DishOverview.class);
    log.debug("======>  findListFromCacheAndDB::dish::cache::{}", Objects.nonNull(dishs)?dishs.size():dishs);
    if(Objects.isNull(dishs)){
      dishs = getDishOverviewCache(pageable);
    }
    return dishs;
  }
  public List<DishOverview> getDishOverviewCache(Pageable pageable){
    String key = RedisKeyConfig.genKeyPageDishOverview(pageable.getPageNumber());
    Page<Dish> dishs = dishRepository.findDishesByStateIn(List.of(DishState.ACTIVE), pageable);
    Map<Object, Object> dishMap = dishs.stream()
        .collect(Collectors.toMap((dish -> dish.getId().toString()), Function.identity()));
    redisService.putHashAll(key, dishMap, RedisService.CacheTime.ONE_DAY);
    return dishMap.values().stream().map(e->new DishOverview((Dish)e)).collect(Collectors.toList());
  }
  /*
  * admin
  * */
  public Page<Dish> findAll(Pageable pageable){
    return dishRepository.findAll(pageable);
  }
  //======================================= DISH DETAIL ===========================
  /*
  * user
  * */
  public Dish getDetailFromCacheAndDb(Long id){
    String key = RedisKeyConfig.genKeyDish(id);
    Dish dish = redisService.getEntity(key, Dish.class);
    log.debug("======> getDetailFromCacheAndDb::dish::cache::{}", dish);
    if(dish != null) return dish;

    dish = dishRepository.findById(id).orElseThrow(() -> new BadRequestException(String.format("dish(id=%d) not found", id)));
    return dish;
  }

  public List<CommentDish> getListCommentFromCacheAndDB(Long dishId, Pageable pageable){
    List<CommentDish> comments = redisService.getHashAll(RedisKeyConfig.genKeyCommentDish(dishId), CommentDish.class);
    log.debug("getListCommentFromCacheAndDB::comments::cache::{}", Objects.nonNull(comments)?comments.size():comments);
    if(comments==null){
      comments = commentDishRepository.findCommentDishesByDish_Id(dishId, pageable);
      log.debug("getListCommentFromCacheAndDB::comments::db::{}", Objects.nonNull(comments)?comments.size():comments);
      if(Objects.nonNull(comments))
        redisService.putHashAll(
          RedisKeyConfig.genKeyCommentDish(dishId),
          comments.stream().collect(Collectors.toMap(c->c.getUser().getId().toString(),c->c)),
          RedisService.CacheTime.ONE_WEEK);
      else comments = new ArrayList<>();
    }
    return comments;
  }
  /*
   * admin
   * */
  public Dish getDetailDishFromDB(Long id){
    return dishRepository.findById(id).orElseThrow(()->new BadRequestException(String.format("dish(id=%d) not found", id)));
  }
  //======================================= SAVE DISH ============================
  /*
  * admin
  * */
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public List<Dish> saveAll(List<Dish> dishs){
    List<Dish> dishsNew = dishRepository.saveAll(dishs);
    redisService.delete(genCacheKeys(null));
    return dishs;
  }
  /*
   * save dish
   * */
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public Dish saveToDB(Dish dish){
    Dish dishNew = dishRepository.save(dish);
    redisService.delete(genCacheKeys(dish));
    return dishNew;
  }

  //==============================================   COMMENT ========================================
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public boolean addCommentDish(CommentDish comment){
    Long dishId = comment.getDish().getId();
    boolean ok = false;
    if(commentDishRepository.findByDishIdAndUserId(dishId, comment.getUser().getId()) != null)
      ok = commentDishRepository.update(comment.getStar(), comment.getComment(), dishId, comment.getUser().getId(), comment.getDate())>0;
    else
      ok = commentDishRepository.save(comment.getStar(), comment.getComment(), dishId, comment.getUser().getId(), comment.getUser().getUsername(), comment.getDate())>=1;
    return ok;
  }

  public void initCommentDishCache(Long dishId){
    // userId, comment
    Map<Object,Object> map = new HashMap<>();
    try {
      List<CommentDish> comments = dishRepository.findById(dishId).orElseThrow(()->new BadRequestException(String.format("dish(id=%d) not found", dishId))).getCommentDishs();
      if(comments==null) comments = new ArrayList<>();
      comments.forEach(c->{
        map.put(c.getUser().getId().toString(), c);
      });
      redisService.putHashAll(RedisKeyConfig.genKeyCommentDish(dishId), map, null);
    }catch (Exception e){
      log.error("======> addToCache::exception::{}",e.getMessage());
    }
  }

  public List<String> genCacheKeys(Dish dish){
    List<String> keys = new ArrayList<>();
    for(int i=0; i<10; i++)
      keys.add(RedisKeyConfig.genKeyPageDishOverview(i));
    if(Objects.nonNull(dish)) keys.add(RedisKeyConfig.genKeyDish(dish.getId()));
    return keys;
  }
}