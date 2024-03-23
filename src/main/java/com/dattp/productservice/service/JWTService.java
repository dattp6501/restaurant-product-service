package com.dattp.productservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JWTService {
  @Value("${jwt.secret}")
  private String SECRET_KEY;

  /*
   * Cac phuong thuc duoi chi su dung khi xac thuc tnanh cong
   * */
  public Long getUserId(){
    return (Long) getDetails().get("id");
  }

  public String getFullname(){
    return getDetails().get("fullname").toString();
  }

  public String getUsername(){
    return getDetails().get("username").toString();
  }

  public String getMail(){
    return getDetails().get("mail").toString();
  }

  @SuppressWarnings(value="unchecked")
  private Map<String, Object> getDetails(){
    return (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getDetails();
  }
  //
}
