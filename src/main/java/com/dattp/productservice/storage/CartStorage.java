package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.pojo.DishOverview;
import com.dattp.productservice.pojo.TableOverview;
import com.dattp.productservice.service.RedisService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartStorage extends Storage{
  @Autowired @Lazy
  private DishStorage dishStorage;
  @Autowired @Lazy
  private TableStorage tableStorage;
  public void addDishToCart(Dish dish){
    redisService.addElemntHash(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()), dish.getId().toString(), new DishOverview(dish), RedisService.CacheTime.ONE_MONTH);
  }

  public void deleteDishInFromCart(Dish dish){
    redisService.deleteHash(RedisKeyConfig.genKeyCartDish(jwtService.getUserId()), dish.getId().toString());
  }

  public List<DishOverview> getListDishInCart(Long userId){
    List<DishOverview> dishOverview = redisService.getHashAll(RedisKeyConfig.genKeyCartDish(userId), DishOverview.class);
    if(syncDishNewInfo(dishOverview)){
      redisService.delete(RedisKeyConfig.genKeyCartDish(userId));
      Map<Object, Object> dishInCartNew = dishOverview.stream()
      .collect(Collectors.toMap((dishOv)->{
        return dishOv.getId().toString();
      }, Function.identity()));
      redisService.putHashAll(RedisKeyConfig.genKeyCartDish(userId), dishInCartNew, RedisService.CacheTime.ONE_MONTH);
    }
    return dishOverview;
  }

  /*
  * if change => return true
  * else return false
   */
  public boolean syncDishNewInfo(List<DishOverview> dishOverviews){
    boolean isChange = false;
    for(DishOverview dishOverview : dishOverviews){
      Dish dish = dishStorage.getDetailFromCacheAndDb(dishOverview.getId());
      if(Objects.isNull(dish)){
        isChange = true;
        dishOverviews.remove(dishOverview);
        continue;
      }
      if(isChangeDishInfo(dish, dishOverview)) isChange = true;
    }
    return isChange;
  }

  private boolean isChangeDishInfo(Dish dish, DishOverview dishOverview){
    boolean isChange = false;
    if(!dish.getName().equals(dishOverview.getName())){
      dishOverview.setName(dish.getName());
      isChange = true;
    }
    if(!dish.getImage().equals(dishOverview.getImage())){
      dishOverview.setImage(dish.getImage());
      isChange = true;
    }
    if(!dish.getPrice().equals(dishOverview.getPrice())){
      dishOverview.setPrice(dish.getPrice());
      isChange = true;
    }
    return isChange;
  }

  //table
  public void addTableToCart(TableE table){
    redisService.addElemntHash(RedisKeyConfig.genKeyCartTable(jwtService.getUserId()), table.getId().toString(), new TableOverview(table), RedisService.CacheTime.ONE_MONTH);
  }

  public List<TableOverview> getListTableInCart(Long userId){
    List<TableOverview> tableOverview = redisService.getHashAll(RedisKeyConfig.genKeyCartTable(userId), TableOverview.class);
    if(syncTableNewInfo(tableOverview)){
      redisService.delete(RedisKeyConfig.genKeyCartTable(userId));
      Map<Object, Object> tableInCartNew = tableOverview.stream()
          .collect(Collectors.toMap((tableOv)->tableOv.getId().toString(), Function.identity()));
      redisService.putHashAll(RedisKeyConfig.genKeyCartDish(userId), tableInCartNew, RedisService.CacheTime.ONE_MONTH);
    }
    return tableOverview;
  }
  /*
   * if change => return true
   * else return false
   */
  public boolean syncTableNewInfo(List<TableOverview> tableOverviews){
    boolean isChange = false;
    for(TableOverview tableOverview : tableOverviews){
      TableE table = tableStorage.getDetailFromCacheAndDb(tableOverview.getId());
      if(Objects.isNull(table)){
        isChange = true;
        tableOverviews.remove(tableOverview);
        continue;
      }
      if(isChangeTableInfo(table, tableOverview)) isChange = true;
    }
    return isChange;
  }
  private boolean isChangeTableInfo(TableE table, TableOverview tableOverview){
    boolean isChange = false;
    if(!table.getName().equals(tableOverview.getName())){
      tableOverview.setName(table.getName());
      isChange = true;
    }
    if(!table.getImage().equals(tableOverview.getImage())){
      tableOverview.setImage(table.getImage());
      isChange = true;
    }
    if(!table.getPrice().equals(tableOverview.getPrice())){
      tableOverview.setPrice(table.getPrice());
      isChange = true;
    }
    return isChange;
  }
}