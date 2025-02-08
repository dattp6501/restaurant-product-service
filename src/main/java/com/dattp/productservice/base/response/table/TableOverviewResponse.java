package com.dattp.productservice.base.response.table;

import com.dattp.productservice.base.response.ProductBaseResponse;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.pojo.PeriodTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TableOverviewResponse extends ProductBaseResponse implements Serializable {
  private List<PeriodTime> freeTime;

  public TableOverviewResponse() {
    super();
  }

  public TableOverviewResponse(TableE table) {
    copyProperties(table);
  }

  public void copyProperties(TableE table) {
    BeanUtils.copyProperties(table, this);
    this.freeTime = new ArrayList<>();
    super.setCreateAtLong(table.getCreateAt());
    super.setUpdateAtLong(table.getUpdateAt());
  }
}