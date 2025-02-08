package com.dattp.productservice.base.dto.resttemplate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseListTableFreeTimeDTO {
  private int code;
  private String message;
  private List<PeriodsTimeBookedTableDTO> data;

  public ResponseListTableFreeTimeDTO() {
    super();
  }
}
