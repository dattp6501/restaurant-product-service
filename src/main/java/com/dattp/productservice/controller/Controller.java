package com.dattp.productservice.controller;

import com.dattp.productservice.service.CartService;
import com.dattp.productservice.service.DishService;
import com.dattp.productservice.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@org.springframework.stereotype.Controller
public class Controller {
  @Autowired @Lazy protected DishService dishService;
  @Autowired @Lazy protected TableService tableService;
  @Autowired @Lazy protected CartService cartService;
}