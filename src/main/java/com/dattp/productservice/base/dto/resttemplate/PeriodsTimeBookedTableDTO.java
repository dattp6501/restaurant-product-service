package com.dattp.productservice.base.dto.resttemplate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PeriodsTimeBookedTableDTO {
  private Long id;
  private String name;
  private int amountOfPeople;
  private float price;

  @JsonFormat(pattern = "HH:mm:ss")
  private Date from;

  @JsonFormat(pattern = "HH:mm:ss")
  private Date to;
  private String description;
  private List<PeriodTimeResponseDTO> times;

  public PeriodsTimeBookedTableDTO() {
  }

  @Override
  public boolean equals(Object obj) {
    return this.id.equals(obj);
  }
}