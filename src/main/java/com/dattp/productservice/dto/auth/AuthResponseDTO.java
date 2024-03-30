package com.dattp.productservice.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
  private String accessToken;
  private String refreshToken;
  private long expiresIn;
  public AuthResponseDTO(String accessToken, String refreshToken, long expiresIn) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
  }
  public AuthResponseDTO() {
  }
}
