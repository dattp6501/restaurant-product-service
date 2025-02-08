package com.dattp.productservice.controller.user.response;

import com.dattp.productservice.base.response.comment.CommentProductResponse;
import com.dattp.productservice.entity.CommentTable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CommentTableResponseDTO extends CommentProductResponse {
  public CommentTableResponseDTO() {
    super();
  }

  public CommentTableResponseDTO(CommentTable c) {
    copyProperties(c);
  }

  public void copyProperties(CommentTable c) {
    BeanUtils.copyProperties(c, this);
    super.setDate(c.getDate());
  }
}