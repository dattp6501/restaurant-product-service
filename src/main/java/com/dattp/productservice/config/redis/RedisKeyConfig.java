package com.dattp.productservice.config.redis;



public class RedisKeyConfig {
  public static final String PREFIX_APP = "restaurant::";

  public static final String PREFIX_PRODUCT = PREFIX_APP + "product::";

  public static final String PREFIX_TABLE = PREFIX_PRODUCT + "table::";

  public static final String PREFIX_DISH = PREFIX_PRODUCT + "dish::";

  public static final long EXPIRE_TIME_DEFAULT = 1000*3600*24*7;

  //=====================================================================================
  //                                      KEY
  //=====================================================================================
  public static String genKeyAllDishOverview(){
    return PREFIX_DISH + "ids";
  }
  public static String genKeyDish(Long dishId){
    return PREFIX_DISH + dishId;
  }

  public static String genKeyTable(Long tableId){
    return PREFIX_TABLE + tableId;
  }
}