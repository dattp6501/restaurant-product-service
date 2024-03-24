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
  @Autowired @Lazy private RedisTemplate<Object, Object> redisTemplate;


  /*
  * hashKey: key cua du lieu
  * key: key cua tung element trong value cua hashKey
  * hasKey => key i : value i
  * */
  public void putHash(String key, String hashKey, Object value, long expireTime){
    try {
      redisTemplate.opsForHash().put(hashKey, key, value);

      if(expireTime == 0) return;
      if(expireTime<=0) expireTime = RedisKeyConfig.EXPIRE_TIME_DEFAULT;
      redisTemplate.expire(hashKey, expireTime, TimeUnit.MILLISECONDS);
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



  public void putHashAll(String hashKey, Map<Object,Object> value, long expireTime){
    try {
      redisTemplate.opsForHash().putAll(hashKey, value);

      if(expireTime == 0) return;
      if(expireTime <= 0) expireTime = RedisKeyConfig.EXPIRE_TIME_DEFAULT;
      redisTemplate.expire(hashKey, expireTime, TimeUnit.MILLISECONDS);
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

  public Boolean hasKey(String key){
    try {
      return redisTemplate.hasKey(key);
    }catch (Exception e){
      return false;
    }
  }

  public void putList(String key, List<Object> list, long expireTime){
    try {
      redisTemplate.opsForList().set(key, list.size(), list);
      if(expireTime == 0) return;
      if(expireTime <= 0) expireTime = RedisKeyConfig.EXPIRE_TIME_DEFAULT;
      redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);

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