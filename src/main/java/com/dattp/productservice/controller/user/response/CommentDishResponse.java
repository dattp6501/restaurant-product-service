package com.dattp.productservice.controller.user.response;

import com.dattp.productservice.base.response.comment.CommentProductResponse;
import com.dattp.productservice.entity.CommentDish;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CommentDishResponse extends CommentProductResponse {
  public CommentDishResponse() {
    super();
  }

  public CommentDishResponse(CommentDish cd) {
    copyProperties(cd);
  }

  public void copyProperties(CommentDish cd) {
    BeanUtils.copyProperties(cd, this);
    super.setDate(cd.getDate());
  }
}
