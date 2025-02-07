package com.dattp.productservice.config.redis;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyConfig {
  public static final String PREFIX_APP = "restaurant::";
  //================================== AUTH ===================
  public static final String PREFIX_AUTH = PREFIX_APP + "auth::";
  public static final String FREFIX_TOKEN = PREFIX_AUTH + "token::";
  //================================== PRODUCT ================
  public static final String PREFIX_PRODUCT = PREFIX_APP + "product::";
  public static final String PREFIX_TABLE = PREFIX_PRODUCT + "table::";
  public static final String PREFIX_DISH = PREFIX_PRODUCT + "dish::";
  //================================== ORDER =======================
  public static final String PREFIX_ORDER = PREFIX_APP + "order::";
  public static final String PREFIX_CART = PREFIX_ORDER + "cart::";
  public static final String PREFIX_CART_DISH = PREFIX_CART + "dish::";
  public static final String PREFIX_CART_TABLE = PREFIX_CART + "table::";

  //=====================================================================================
  //                                      KEY
  //=====================================================================================
  //================================== PRODUCT ==========================================
  public static String genKeyPage(Pageable pageable) {
    return String.format("page:%d_%d", pageable.getPageNumber(), pageable.getPageSize());
  }

  public static String genKeyPageDishOverview(int page) {
    return String.format("%soverview::page:%d", PREFIX_DISH, page);
  }

  public static String genKeyDish(Long dishId) {
    return PREFIX_DISH + dishId;
  }

  public static String genKeyCommentDish(Long dishId) {
    return PREFIX_DISH + dishId + "::comment";
  }

  public static String genKeyPageTableOverview(int page) {
    return String.format("%soverview::page::%d", PREFIX_TABLE, page);
  }

  public static String genKeyTable(Long tableId) {
    return PREFIX_TABLE + tableId;
  }

  public static String genKeyTableFreeTime(Long tableId) {
    return String.format("%s%d::free_time", PREFIX_TABLE, tableId);
  }

  public static String genKeyCommentTable(Long tableId) {
    return PREFIX_TABLE + tableId + "::comment";
  }

  //================================== AUTH ==========================================
  public static String genKeyToken(Long userId) {
    return FREFIX_TOKEN + userId;
  }

  //================================== PRODUCT ==========================================
  public static String genKeyCartDish(Long userId) {
    return PREFIX_CART_DISH + userId;
  }

  public static String genKeyCartTable(Long userId) {
    return PREFIX_CART_TABLE + userId;
  }
}