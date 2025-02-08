package com.dattp.productservice.base.response.table;

import com.dattp.productservice.base.response.ProductBaseResponse;
import com.dattp.productservice.controller.user.response.CommentTableResponseDTO;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.state.TableState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TableBaseResponse extends ProductBaseResponse {
  private TableState state;

  private Integer amountOfPeople;

  @JsonFormat(pattern = "HH:mm")
  private Date from;

  @JsonFormat(pattern = "HH:mm")
  private Date to;

  private List<CommentTableResponseDTO> comments;

  public TableBaseResponse() {
    super();
  }

  public TableBaseResponse(TableE table) {
    copyProperties(table);
  }

  public void copyProperties(TableE table) {
    BeanUtils.copyProperties(table, this);
    super.setCreateAt(table.getCreateAt());
    super.setUpdateAt(table.getUpdateAt());
  }
}
