package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.exception.BadRequestException;
import com.dattp.productservice.pojo.DishOverview;
import com.dattp.productservice.service.RedisService;
import com.dattp.productservice.utils.JSONUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DishStorage extends Storage{
  //===================================== LIST DISH ==========================================
  /*
   * user
   * */
  public List<Dish> findListFromCacheAndDB(Pageable pageable){
    String key = RedisKeyConfig.genKeyAllDishOverview();
    List<DishOverview> dishs = redisService.getHashAll(key, DishOverview.class);
    if(dishs == null){
      dishs = initDishOverviewCache();
    }
    //
    return dishs.stream()
      .map(DishOverview::toDish)
      .collect(Collectors.toList());
  }
  public List<DishOverview> initDishOverviewCache(){
    Map<Object, Object> dishMap = new HashMap<>();
    try{
      List<Dish> dishs = dishRepository.findAllByStateIn(List.of(DishState.ACTIVE));
      for(Dish d : dishs){
        DishOverview dishOverview = new DishOverview(d);
        dishMap.put(d.getId().toString(), dishOverview);
      }
      redisService.putHashAll(RedisKeyConfig.genKeyAllDishOverview(), dishMap, RedisService.CacheTime.NO_LIMIT);
    }catch (Exception e){
      e.printStackTrace();
    }
    return dishMap.values().stream().map(e->(DishOverview)e).collect(Collectors.toList());
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
    if(dish != null) return dish;

    dish = dishRepository.findById(id).orElseThrow(() -> new BadRequestException(String.format("dish(id=%d) not found", id)));
    addToCache(dish);

    return dish;
  }

  public List<CommentDish> getListCommentFromCacheAndDB(Long dishId, Pageable pageable){
    List<CommentDish> comments = redisService.getHashAll(RedisKeyConfig.genKeyCommentDish(dishId), CommentDish.class);
    if(comments==null){
      comments = commentDishRepository.findCommentDishesByDish_Id(dishId, pageable);
      redisService.putHashAll(
        RedisKeyConfig.genKeyCommentDish(dishId),
        comments.stream().collect(Collectors.toMap(c->c.getUser().getId().toString(),c->c)),
        RedisService.CacheTime.ONE_WEEK);
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
    return dishRepository.saveAll(dishs);
  }
  /*
   * save dish
   * */
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public Dish saveToDB(Dish dish){
    return dishRepository.save(dish);
  }

  public void addToCache(Dish dish){
    try{
      redisService.setEntity(RedisKeyConfig.genKeyDish(dish.getId()), dish, null);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void addOverviewDishToCache(Dish dish){
    try {
      //neu key luu ton tai => them
      if(redisService.hasKey(RedisKeyConfig.genKeyAllDishOverview())){
        redisService.addElemntHash(RedisKeyConfig.genKeyAllDishOverview(), dish.getId().toString(), new DishOverview(dish), RedisService.CacheTime.NO_LIMIT);
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
        redisService.updateHash(RedisKeyConfig.genKeyAllDishOverview(), dish.getId().toString(), new DishOverview(dish), RedisService.CacheTime.NO_LIMIT);
        return;
      }
      //khoi tao lai danh sach
      initDishOverviewCache();
    }catch (Exception e){
      e.printStackTrace();
    }
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
      e.printStackTrace();
    }
  }
}