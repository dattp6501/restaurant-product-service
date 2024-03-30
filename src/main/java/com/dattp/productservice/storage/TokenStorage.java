package com.dattp.productservice.storage;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.dto.auth.AuthResponseDTO;
import com.dattp.productservice.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TokenStorage extends Storage{
  public AuthResponseDTO get(Long userId){
    AuthResponseDTO token = redisService.getEntity(RedisKeyConfig.genKeyToken(userId), AuthResponseDTO.class);
    if(Objects.isNull(token)) throw new BadRequestException("token invalid");
    return token;
  }
}