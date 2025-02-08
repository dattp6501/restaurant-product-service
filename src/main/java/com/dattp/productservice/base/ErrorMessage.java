package com.dattp.productservice.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
  SUCCESS(HttpStatus.OK, "Thành công"),
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "Dư liệu không hợp lệ"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Không đủ quyền truy cập"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Hệ thống bận");

  private final HttpStatus status;
  private final String message;

  private ErrorMessage(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
