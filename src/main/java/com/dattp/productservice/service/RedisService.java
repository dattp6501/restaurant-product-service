package com.dattp.productservice.service;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisService {
  public static enum CacheTime {
    ONE_DAY(1000*3600*24L),
    THREE_DAY(1000*3600*24*3L),
    ONE_WEEK(1000*3600*24*7L),
    ONE_MONTH(1000*3600*24*30L),
    NO_LIMIT(0L);

    private final long expireTime;
    CacheTime(long expireTime){
      this.expireTime = expireTime;
    }

    public long time(){
      return this.expireTime;
    }
  }


  @Autowired @Lazy private RedisTemplate<Object, Object> redisTemplate;
  //======================================== GENERAL ============================================
  public void delete(String key){
    try {
      redisTemplate.delete(key);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public Boolean hasKey(String key){
    try {
      return redisTemplate.hasKey(key);
    }catch (Exception e){
      return false;
    }
  }
  //========================================== HASH ===============================================
  /*
  * key: key cua du lieu
  * hashKey: key cua tung element trong value cua hashKey
  * key => hasKey i : value i
  * */
  public void deleteHash(String key, String hashKey){
    try {
      redisTemplate.opsForHash().delete(key, hashKey);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public Object getHash(String key, String hashKey){
    try {
      return redisTemplate.opsForHash().get(key, hashKey);
    }catch (Exception e){
      return null;
    }
  }

  public void updateHash(String key, String hashKey, Object value, CacheTime cacheTime){
    try {
      deleteHash(key, hashKey);
      redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);

      if(cacheTime == CacheTime.NO_LIMIT) return;
      if(cacheTime == null) cacheTime = CacheTime.ONE_WEEK;
      redisTemplate.expire(key, cacheTime.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void addElemntHash(String key, String hashKey, Object value, CacheTime cacheTime){
    try {
      deleteHash(key, hashKey);
      redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);

      if(cacheTime == CacheTime.NO_LIMIT) return;
      if(cacheTime == null) cacheTime = CacheTime.ONE_WEEK;
      redisTemplate.expire(key, cacheTime.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void putHashAll(String key, Map<Object,Object> value, CacheTime cacheTime){
    try {
      redisTemplate.opsForHash().putAll(key, value);

      if(cacheTime == CacheTime.NO_LIMIT) return;
      if(cacheTime == null) cacheTime = CacheTime.ONE_WEEK;
      redisTemplate.expire(key, cacheTime.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public <T> List<T> getHashAll(String key, Class<T> typeClassElement){
    try {
      List<T> list = redisTemplate.opsForHash().entries(key).values()
        .stream()
        .map(e->JSONUtils.toEntity((String) e,typeClassElement))
        .collect(Collectors.toList());
      return list.isEmpty()?null:list;
    }catch (Exception e){
      return null;
    }
  }

  //==================================== LIST =============================================
  public void putList(String key, List<Object> list, CacheTime cacheTime){
    try {
      redisTemplate.opsForList().set(key, list.size(), list);
      if(cacheTime == CacheTime.NO_LIMIT) return;
      if(cacheTime == null) cacheTime = CacheTime.ONE_WEEK;
      redisTemplate.expire(key, cacheTime.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public List<Object> getList(String key, Pageable pageable){
    try {
      return redisTemplate.opsForList().range(key, (long) pageable.getPageSize() *pageable.getPageNumber(), pageable.getPageSize());
    }catch (Exception e){
      return new ArrayList<>();
    }
  }

  //====================================================== STRING ==================================
  public void setEntity(String key, Object value, CacheTime cacheTime){
    try {
      if(cacheTime == null) cacheTime = CacheTime.ONE_WEEK;

      if(cacheTime == CacheTime.NO_LIMIT){
        redisTemplate.opsForValue().set(key, value);
      }
      redisTemplate.opsForValue().set(key, value, cacheTime.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public <T> T getEntity(String key, Class<T> tClass){
    try {
      return JSONUtils.toEntity((String) redisTemplate.opsForValue().get(key), tClass);
    }catch (Exception e){
      e.printStackTrace();
      return null;
    }
  }

}