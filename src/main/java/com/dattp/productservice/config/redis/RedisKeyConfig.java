package com.dattp.productservice.config.redis;



public class RedisKeyConfig {
  public static final String PREFIX_APP = "restaurant::";

  public static final String PREFIX_PRODUCT = PREFIX_APP + "product::";

  public static final String PREFIX_TABLE = PREFIX_PRODUCT + "table::";

  public static final String PREFIX_DISH = PREFIX_PRODUCT + "dish::";

  //=====================================================================================
  //                                      KEY
  //=====================================================================================
  public static String genKeyAllDishOverview(){
    return PREFIX_DISH + "overview";
  }
  public static String genKeyDish(Long dishId){
    return PREFIX_DISH + dishId;
  }

  public static String genKeyCommentDish(Long dishId){return PREFIX_DISH + dishId + "::comment";}

  public static String genKeyAllTableOverview(){
    return PREFIX_TABLE + "overview";
  }
  public static String genKeyTable(Long tableId){
    return PREFIX_TABLE + tableId;
  }
  public static String genKeyCommentTable(Long tableId){return PREFIX_TABLE + tableId + "::comment";}
}