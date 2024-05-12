package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.entity.CommentTable;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.state.TableState;
import com.dattp.productservice.exception.BadRequestException;
import com.dattp.productservice.pojo.PeriodTime;
import com.dattp.productservice.pojo.TableOverview;
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
public class TableStorage extends Storage{
  //================================  LIST TABLE =========================================
  /*
  * user
  * */
  public List<TableOverview> findListFromCacheAndDB(Pageable pageable){
    String key = RedisKeyConfig.genKeyPageTableOverview(pageable.getPageNumber());
    List<TableOverview> list = redisService.getHashAll(key, TableOverview.class);
    if(Objects.isNull(list)){
      list = getTableOverview(pageable);
    }
    //get free time
    if(Objects.nonNull(list)){
      list.forEach(t->{
        String keyFree = RedisKeyConfig.genKeyTableFreeTime(t.getId());
        List<PeriodTime> tableFreeTime = redisService.getList(keyFree, PeriodTime.class);
        if(Objects.nonNull(tableFreeTime)) t.setFreeTime(tableFreeTime);
      });
    }
    return list;
  }

  public List<TableOverview> getTableOverview(Pageable pageable){
    String key = RedisKeyConfig.genKeyPageTableOverview(pageable.getPageNumber());
    Page<TableE> tableS = tableRepository.findAllByStateIn(List.of(TableState.ACTIVE), pageable);
    Map<Object, Object> tableMap = tableS.stream()
        .collect(Collectors.toMap(table->table.getId().toString(), Function.identity()));
    redisService.putHashAll(key, tableMap, RedisService.CacheTime.ONE_DAY);
    return tableMap.values().stream()
      .map(e->new TableOverview((TableE)e)).collect(Collectors.toList());
  }

  public TableE getDetailFromCacheAndDb(Long tableId){
    return new TableE();
  }
  /*
  * admin
  * */
  public Page<TableE> findAll(Pageable pageable){
    return tableRepository.findAll(pageable);
  }
  public List<TableE> findAllTableReadyByIdInAndStateIn(List<Long> ids){
    return tableRepository.findAllByIdInAndStateIn(ids, TableE.getListStatusReady());
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
  public List<TableE> saveAllToDB(List<TableE> tables){
    List<TableE> tablesNew = tableRepository.saveAll(tables);
    redisService.delete(genCacheKeys(null));
    return tablesNew;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public TableE saveToDB(TableE table){
    TableE tableNew = tableRepository.save(table);
    redisService.delete(genCacheKeys(tableNew));
    return tableNew;
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

  public List<String> genCacheKeys(TableE table){
    List<String> keys = new ArrayList<>();
    for(int i=0; i<10; i++)
      keys.add(RedisKeyConfig.genKeyPageTableOverview(i));
    if(Objects.nonNull(table)) keys.add(RedisKeyConfig.genKeyTable(table.getId()));
    return keys;
  }
}
