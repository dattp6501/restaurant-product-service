package com.dattp.productservice.controller.user.response;

import com.dattp.productservice.base.response.table.TableBaseResponse;
import com.dattp.productservice.entity.TableE;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableUserResponse extends TableBaseResponse {
  public TableUserResponse() {
    super();
  }

  public TableUserResponse(TableE table) {
    this.copyProperties(table);
  }

  public void copyProperties(TableE table) {
    super.copyProperties(table);
  }
}