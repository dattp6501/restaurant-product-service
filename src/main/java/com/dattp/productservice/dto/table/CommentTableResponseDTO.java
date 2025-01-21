package com.dattp.productservice.dto.table;

import com.dattp.productservice.entity.CommentTable;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentTableResponseDTO {
  private Long id;
  private int star;
  private String comment;
  private User user;

  @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
  private LocalDateTime date;

  public CommentTableResponseDTO() {
  }

  public CommentTableResponseDTO(CommentTable ct) {
    copyProperties(ct);
  }

  public void copyProperties(CommentTable ct) {
    BeanUtils.copyProperties(ct, this);
    this.date = DateUtils.convertToLocalDateTime(ct.getDate());
  }
}