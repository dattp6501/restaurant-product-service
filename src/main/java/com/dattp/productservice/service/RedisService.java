package com.dattp.productservice.service;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
  * hashKey: key cua du lieu
  * key: key cua tung element trong value cua hashKey
  * hasKey => key i : value i
  * */
  public void deleteHash(String key, String hashKey){
    try {
      redisTemplate.opsForHash().delete(hashKey, key);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public Object getHash(String key, String hashKey){
    try {
      return redisTemplate.opsForHash().get(hashKey, key);
    }catch (Exception e){
      return null;
    }
  }

  public void createHash(String key, String hashKey, Object value, CacheTime cacheTime){
    try {
      delete(hashKey);
      redisTemplate.opsForHash().put(hashKey, key, value);

      if(cacheTime == CacheTime.NO_LIMIT) return;
      if(cacheTime == null) cacheTime = CacheTime.ONE_WEEK;
      redisTemplate.expire(hashKey, cacheTime.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void updateHash(String key, String hashKey, Object value){
    try {
      deleteHash(key, hashKey);
      redisTemplate.opsForHash().putIfAbsent(hashKey, key, value);
      redisTemplate.expire(hashKey, CacheTime.ONE_WEEK.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void addElemntHash(String key, String hashKey, Object value){
    try {
      deleteHash(key, hashKey);
      redisTemplate.opsForHash().putIfAbsent(hashKey, key, value);
      redisTemplate.expire(hashKey, CacheTime.ONE_WEEK.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void putHashAll(String hashKey, Map<Object,Object> value, CacheTime cacheTime){
    try {
      redisTemplate.opsForHash().putAll(hashKey, value);

      if(cacheTime == CacheTime.NO_LIMIT) return;
      if(cacheTime == null) cacheTime = CacheTime.ONE_WEEK;
      redisTemplate.expire(hashKey, cacheTime.time(), TimeUnit.MILLISECONDS);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public Map<Object, Object> getHashAll(String hashKey){
    try {
      return redisTemplate.opsForHash().entries(hashKey);
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



}