package com.dattp.productservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {
  @Value("${jwt.secret}")
  private String SECRET_KEY;

  /*
   * Cac phuong thuc duoi chi su dung khi xac thuc tnanh cong
   * */
  public Long getUserId() {
    return (Long) getDetails().get("id");
  }

  public String getFullname() {
    return getDetails().get("fullname").toString();
  }

  public String getUsername() {
    return getDetails().get("username").toString();
  }

  public String getMail() {
    return getDetails().get("mail").toString();
  }

  @SuppressWarnings(value = "unchecked")
  private Map<String, Object> getDetails() {
    return (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getDetails();
  }

  //
  public Map<String, Object> getDetail(String accessToken) {
    // tao giai thuat giai ma
    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
    // giai ma
    DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);
    Map<String, Object> detail = new HashMap<>();
    detail.put("id", decodedJWT.getClaim("id").asLong());
    detail.put("fullname", decodedJWT.getClaim("fullname").asString());
    detail.put("username", decodedJWT.getClaim("username").asString());
    detail.put("email", decodedJWT.getClaim("email").asString());
    detail.put("roles", decodedJWT.getClaim("roles").asArray(String.class));
    return detail;
  }
}
