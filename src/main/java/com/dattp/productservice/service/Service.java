package com.dattp.productservice.service;

import com.dattp.productservice.repository.CommentDishRepository;
import com.dattp.productservice.repository.CommentTableRepository;
import com.dattp.productservice.repository.DishRepository;
import com.dattp.productservice.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
public class Service {
  @Autowired @Lazy protected RestTemplate restTemplate;
  @Autowired @Lazy protected JWTService jwtService;
  @Autowired @Lazy protected DishRepository dishRepository;
  @Autowired @Lazy protected CommentDishRepository CommentDishRepository;
  @Autowired @Lazy protected TableRepository tableRepository;
  @Autowired @Lazy protected CommentTableRepository commentTableRepository;
}