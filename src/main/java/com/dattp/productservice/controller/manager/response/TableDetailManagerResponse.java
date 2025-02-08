package com.dattp.productservice.controller.manager.response;

import com.dattp.productservice.base.response.table.TableDetailBaseResponse;
import com.dattp.productservice.entity.TableE;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableDetailManagerResponse extends TableDetailBaseResponse {
  public TableDetailManagerResponse() {
    super();
  }

  public TableDetailManagerResponse(TableE table) {
    this.copyProperties(table);
  }

  @Override
  public void copyProperties(TableE table) {
    super.copyProperties(table);
  }
}
