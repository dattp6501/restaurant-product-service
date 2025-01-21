package com.dattp.productservice.service;

import com.dattp.productservice.dto.auth.AuthResponseDTO;
import com.dattp.productservice.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService extends com.dattp.productservice.service.Service {
  public Map<String, Object> verify(String accessToken) {
    Map<String, Object> detail = jwtService.getDetail(accessToken);

    if (cacheEnable) {
      AuthResponseDTO tokenOld = tokenStorage.get((Long) detail.get("id"));
      if (!tokenOld.getAccessToken().equals(accessToken)) throw new BadRequestException("Token invalid");
    }

    return detail;
  }
}