package com.dattp.productservice.pojo;

import com.dattp.productservice.entity.TableE;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TableOverview implements Serializable {
  private Long id;
  private String name;
  private String image;
  private List<Object> freeTime;

  public TableOverview(){}

  public TableOverview(TableE table){
    copyProperties(table);
  }

  public void copyProperties(TableE table){
    BeanUtils.copyProperties(table, this);
    this.freeTime = new ArrayList<>();
  }
}