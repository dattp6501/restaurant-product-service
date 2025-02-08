package com.dattp.productservice.utils;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;

public class JSONUtils {
  private static ObjectMapper mObjectMapper;

  private static ObjectMapper getMapper() {
    if (mObjectMapper == null) {
      mObjectMapper = new ObjectMapper();
      mObjectMapper
          .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
          .registerModule(new ParameterNamesModule())
          .registerModule(new Jdk8Module())
          .registerModule(new JavaTimeModule())
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//          .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
//          .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
//          .activateDefaultTyping(
//              LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL
//          )
      ;
    }
    return mObjectMapper;
  }

  public static <T> T toEntity(String json, Class<T> tClass) {
    try {
      if (json == null || json.isEmpty()) return null;
      return getMapper().readValue(json, tClass);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public static String toJson(Object entity) {
    try {
      return getMapper().writeValueAsString(entity);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public static <T> T toStringJson(byte[] bytes, Class<T> tClass) {
    try {
      if (bytes == null) return null;
      return getMapper().readValue(bytes, tClass);
    } catch (IOException e) {
      return null;
    }
  }

  public static byte[] toByteArray(Object data) {
    try {
      return getMapper().writeValueAsBytes(data);
    } catch (JsonProcessingException e) {
      return null;
    }
  }
}