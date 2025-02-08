package com.dattp.productservice.storage;


import com.dattp.productservice.repository.CommentDishRepository;
import com.dattp.productservice.repository.CommentTableRepository;
import com.dattp.productservice.repository.DishRepository;
import com.dattp.productservice.repository.TableRepository;
import com.dattp.productservice.service.JWTService;
import com.dattp.productservice.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class Storage {
  @Value("${cache.enable}")
  protected boolean cacheEnable;

  @Autowired
  @Lazy
  protected RedisService redisService;
  @Autowired
  @Lazy
  protected JWTService jwtService;

  @Autowired
  @Lazy
  protected TableRepository tableRepository;
  @Autowired
  @Lazy
  protected CommentTableRepository commentTableRepository;
}