package com.dattp.productservice.base.response.comment;

import com.dattp.productservice.entity.User;
import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentProductResponse {
  private Long id;

  private int star;

  private String comment;

  private User user;

  @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
  private LocalDateTime date;

  public CommentProductResponse() {
    super();
  }

  public void setDate(Long date) {
    this.date = DateUtils.convertToLocalDateTime(date);
  }
}
