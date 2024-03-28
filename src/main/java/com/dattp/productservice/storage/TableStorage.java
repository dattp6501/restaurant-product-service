package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.CommentTable;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.state.TableState;
import com.dattp.productservice.exception.BadRequestException;
import com.dattp.productservice.pojo.TableOverview;
import com.dattp.productservice.service.RedisService;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class TableStorage extends Storage{
  //================================  LIST TABLE =========================================
  /*
  * user
  * */
  public List<TableOverview> findListFromCacheAndDB(Pageable pageable){
    String key = RedisKeyConfig.genKeyAllTableOverview();
    List<TableOverview> list = redisService.getHashAll(key, TableOverview.class);

    if(list==null) list = initTableOverview();

    return list;
  }

  public List<TableOverview> initTableOverview(){
    Map<Object, Object> tableMap = new HashMap<>();
    try{
      List<TableE> tableS = tableRepository.findAllByStateIn(List.of(TableState.ACTIVE));
      for(TableE t : tableS){
        tableMap.put(t.getId().toString(), new TableOverview(t));
      }
      redisService.putHashAll(RedisKeyConfig.genKeyAllTableOverview(), tableMap, RedisService.CacheTime.NO_LIMIT);
    }catch (Exception e){
      log.error("======> addToCache::exception::{}",e.getMessage());
    }
    return tableMap.values().stream()
      .map(e->(TableOverview)e).collect(Collectors.toList());
  }
  /*
  * admin
  * */
  public Page<TableE> findAll(Pageable pageable){
    return tableRepository.findAll(pageable);
  }
  //================================ TABLE DETAIL =======================================
  /*
  * user
  * */
  public TableE getDetailFromCacheAndDB(Long id){
    String key = RedisKeyConfig.genKeyTable(id);
    TableE table = redisService.getEntity(key, TableE.class);
    if(table!=null) return table;

    table = tableRepository.findById(id).orElseThrow(()-> new BadRequestException(String.format("table(id=%d) not found", id)));
    addToCache(table);
    return table;
  }
  /*
  * admin
  * */
  public TableE getDetailFromDB(Long id){
    return tableRepository.findById(id).orElseThrow(()-> new BadRequestException(String.format("table(id=%d) not found", id)));
  }

  //=================================== SAVE TABLE ========================
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public TableE saveToDB(TableE table){
    return tableRepository.save(table);
  }

  public void addToCache(TableE table){
    try {
      redisService.setEntity(RedisKeyConfig.genKeyTable(table.getId()), table, null);
    }catch (Exception e){
      log.error("======> addToCache::exception::{}",e.getMessage());
    }
  }

  public void addTableOverview(TableE table){
    try {
      if(redisService.hasKey(RedisKeyConfig.genKeyTable(table.getId()))){
        redisService.addElemntHash(RedisKeyConfig.genKeyAllTableOverview(), table.getId().toString(), new TableOverview(table), RedisService.CacheTime.NO_LIMIT);
        return;
      }
      initTableOverview();
    }catch (Exception e){
      log.error("======> addToCache::exception::{}",e.getMessage());
    }
  }

  public void updateTableOverview(TableE table){
    try {
      if(redisService.hasKey(RedisKeyConfig.genKeyTable(table.getId()))){
        redisService.updateHash(RedisKeyConfig.genKeyAllTableOverview(), table.getId().toString(), new TableOverview(table), RedisService.CacheTime.NO_LIMIT);
        return;
      }
      initTableOverview();
    }catch (Exception e){
      log.error("======> addToCache::exception::{}",e.getMessage());
    }
  }



  //=======================================    COMMENT  =============================================
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public boolean addCommentTable(CommentTable comment){
    Long tableId = comment.getTable().getId();
    boolean ok = false;
    if(commentTableRepository.findByTableIdAndUserId(tableId, comment.getUser().getId())!=null)
      ok = commentTableRepository.update(comment.getStar(), comment.getComment(), tableId, comment.getUser().getId(), comment.getDate())>0;
    else
      ok = commentTableRepository.save(comment.getStar(), comment.getComment(), tableId, comment.getUser().getId(), comment.getUser().getUsername(), comment.getDate())>=1;
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
      log.error("======> addToCache::exception::{}",e.getMessage());
    }
  }

  public List<CommentTable> getListCommentTableFromCacheAndDB(Long tableId, Pageable pageable){
    List<CommentTable> comments = redisService.getHashAll(RedisKeyConfig.genKeyCommentTable(tableId), CommentTable.class);
    if(comments == null){
      comments = commentTableRepository.findCommentTablesByTableId(tableId, pageable).toList();
      redisService.putHashAll(
        RedisKeyConfig.genKeyCommentTable(tableId),
        comments.stream().collect(Collectors.toMap(c->c.getUser().getId().toString(), c->c)),
        RedisService.CacheTime.ONE_WEEK
      );
    }
    return comments;
  }
}
