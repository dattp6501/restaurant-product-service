package com.dattp.productservice.base.response;

import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductBaseResponse {
  private Long id;

  private String name;

  private Float price;

  private String image;

  private String description;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private LocalDateTime createAt;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private LocalDateTime updateAt;

  public ProductBaseResponse() {
    super();
  }

  public void setCreateAtLong(Long createAt) {
    if (createAt != null) this.createAt = DateUtils.convertToLocalDateTime(createAt);
  }

  public void setUpdateAtLong(Long updateAt) {
    if (updateAt != null) this.updateAt = DateUtils.convertToLocalDateTime(updateAt);
  }
}
