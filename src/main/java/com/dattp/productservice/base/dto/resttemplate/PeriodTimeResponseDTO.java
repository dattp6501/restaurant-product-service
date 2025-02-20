package com.dattp.productservice.base.dto.resttemplate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;

@Getter
public class PeriodTimeResponseDTO {
  @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
  private Date from;

  @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
  private Date to;

  public PeriodTimeResponseDTO() {
  }

  public PeriodTimeResponseDTO(Date from, Date to) {
    this.from = from;
    this.to = to;
  }
}
