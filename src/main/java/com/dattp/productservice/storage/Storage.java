package com.dattp.productservice.storage;


import com.dattp.productservice.repository.CommentDishRepository;
import com.dattp.productservice.repository.CommentTableRepository;
import com.dattp.productservice.repository.DishRepository;
import com.dattp.productservice.repository.TableRepository;
import com.dattp.productservice.service.JWTService;
import com.dattp.productservice.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class Storage {
  @Autowired @Lazy protected RedisService redisService;
  @Autowired @Lazy protected JWTService jwtService;

  @Autowired @Lazy protected DishRepository dishRepository;
  @Autowired @Lazy protected CommentDishRepository commentDishRepository;
  @Autowired @Lazy protected TableRepository tableRepository;
  @Autowired @Lazy protected CommentTableRepository commentTableRepository;
}