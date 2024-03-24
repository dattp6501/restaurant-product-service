package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.CommentTable;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.entity.state.TableState;
import com.dattp.productservice.pojo.DishOverview;
import com.dattp.productservice.pojo.TableOverview;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TableStorage extends Storage{
  //================================ TABLE DETAIL =======================================
  public TableE getDetailFromCacheAndDB(Long id){
    TableE table = null;
    String hashKey = RedisKeyConfig.genKeyTable(id);
    if(redisService.hasKey(hashKey)) table = (TableE) redisService.getHash(id.toString(), hashKey);
    else{
      table = tableRepository.findById(id).orElseThrow();
      addToCache(table);
    }
    return table;
  }

  public TableE getDetailFromDB(Long id){
    return tableRepository.findById(id).orElseThrow();
  }

  public TableE addToCacheAndDB(TableE table){
    table = tableRepository.save(table);
    try {
      //detail
      addToCache(table);
      //overview
      addTableOverview(table);
    }catch (Exception e){
      e.printStackTrace();
    }
    return table;
  }

  public TableE updateFromCacheAndDB(TableE table){
    table = tableRepository.save(table);
    try {
      //detail
      table.setCommentTables(null);
      redisService.updateHash(table.getId().toString(), RedisKeyConfig.genKeyTable(table.getId()), table);
      //overview
      updateTableOverview(table);
    }catch (Exception e){
      e.printStackTrace();
    }
    return table;
  }

  public void addToCache(TableE table){
    try {
      table.setCommentTables(null);
      redisService.createHash(table.getId().toString(), RedisKeyConfig.genKeyTable(table.getId()), table, null);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  //=================================== TABLE OVERVIEW ===================================
  public List<TableOverview> findListFromCacheAndDB(Pageable pageable){
    String keyCache = RedisKeyConfig.genKeyAllTableOverview();
    Map<Object, Object> tableMap = null;
    if(redisService.hasKey(keyCache)) tableMap = redisService.getHashAll(keyCache);
    else tableMap = initTableOverview();
    //
    return tableMap.values().stream()
      .map(o->(TableOverview)o)
      .collect(Collectors.toList());
  }

  public void addTableOverview(TableE table){
    try {
      if(redisService.hasKey(RedisKeyConfig.genKeyTable(table.getId()))){
        redisService.addElemntHash(table.getId().toString(), RedisKeyConfig.genKeyAllTableOverview(), new TableOverview(table));
        return;
      }
      initTableOverview();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void updateTableOverview(TableE table){
    try {
      if(redisService.hasKey(RedisKeyConfig.genKeyTable(table.getId()))){
        redisService.updateHash(table.getId().toString(), RedisKeyConfig.genKeyAllTableOverview(), new TableOverview(table));
        return;
      }
      initTableOverview();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public Map<Object, Object> initTableOverview(){
    Map<Object, Object> tableMap = new HashMap<>();
    try{
      List<TableE> tableS = tableRepository.findAllByStateIn(List.of(TableState.ACTIVE));
      for(TableE t : tableS){
        tableMap.put(t.getId().toString(), new TableOverview(t));
      }
      redisService.putHashAll(RedisKeyConfig.genKeyAllTableOverview(), tableMap, null);
    }catch (Exception e){
      e.printStackTrace();
    }
    return tableMap;
  }

  //=======================================    COMMENT  =============================================
  public boolean addCommentTable(CommentTable comment){
    Long tableId = comment.getTable().getId();
    boolean ok = false;
    if(commentTableRepository.findByTableIdAndUserId(tableId, comment.getUser().getId())!=null)
      ok = commentTableRepository.update(comment.getStar(), comment.getComment(), tableId, comment.getUser().getId(), comment.getDate())>0;
    else
      ok = commentTableRepository.save(comment.getStar(), comment.getComment(), tableId, comment.getUser().getId(), comment.getUser().getUsername(), comment.getDate())>=1;
    //cache
    String hashKey = RedisKeyConfig.genKeyCommentTable(tableId);
    if(!redisService.hasKey(hashKey)) initCommentTableCache(tableId);
    redisService.addElemntHash(comment.getUser().getId().toString(), hashKey, comment);
    return ok;
  }


  public void initCommentTableCache(Long tableId){
    // userId, comment
    Map<Object,Object> map = new HashMap<>();
    try {
      List<CommentTable> comments = tableRepository.findById(tableId).orElseThrow().getCommentTables();
      if(comments==null) comments = new ArrayList<>();
      comments.forEach(t->{
        map.put(t.getUser().getId().toString(), t);
      });
      redisService.putHashAll(RedisKeyConfig.genKeyCommentTable(tableId), map, null);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
