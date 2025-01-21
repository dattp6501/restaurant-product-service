package com.dattp.productservice.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PeriodTime implements Serializable {
  private Long from;
  private Long to;
}