package com.dattp.productservice.controller.manager.response;

import com.dattp.productservice.base.response.table.TableBaseResponse;
import com.dattp.productservice.entity.TableE;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableManagerResponse extends TableBaseResponse {
  public TableManagerResponse() {
    super();
  }

  public TableManagerResponse(TableE table) {
    this.copyProperties(table);
  }

  public void copyProperties(TableE table) {
    super.copyProperties(table);
  }
}
