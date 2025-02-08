package com.dattp.productservice.base.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseResponse {
  private int code;
  private String message;
  private Object data;
}