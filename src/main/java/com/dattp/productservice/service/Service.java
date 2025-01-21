package com.dattp.productservice.service;

import com.dattp.productservice.repository.CommentDishRepository;
import com.dattp.productservice.repository.CommentTableRepository;
import com.dattp.productservice.repository.TableRepository;
import com.dattp.productservice.storage.CartStorage;
import com.dattp.productservice.storage.DishStorage;
import com.dattp.productservice.storage.TableStorage;
import com.dattp.productservice.storage.TokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
public class Service {
  @Value("${cache.enable}")
  protected boolean cacheEnable;

  @Autowired
  @Lazy
  protected RestTemplate restTemplate;
  @Autowired
  @Lazy
  protected JWTService jwtService;
  @Autowired
  @Lazy
  protected RedisService redisService;

  @Autowired
  @Lazy
  protected DishStorage dishStorage;
  @Autowired
  @Lazy
  protected TableStorage tableStorage;
  @Autowired
  @Lazy
  protected TokenStorage tokenStorage;
  @Autowired
  @Lazy
  protected CartStorage cartStorage;

  @Autowired
  @Lazy
  protected KafkaService kafkaService;

  @Autowired
  @Lazy
  protected CommentDishRepository CommentDishRepository;
  @Autowired
  @Lazy
  protected TableRepository tableRepository;
  @Autowired
  @Lazy
  protected CommentTableRepository commentTableRepository;
}