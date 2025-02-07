package com.dattp.productservice.controller.user.response;

import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.state.TableState;
import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TableResponseDTO {
  private TableState state;

  private Long id;

  private String name;

  private String image;

  private Integer amountOfPeople;

  private Float price;

  @JsonFormat(pattern = "HH:mm")
  private Date from;

  @JsonFormat(pattern = "HH:mm")
  private Date to;

  private List<CommentTableResponseDTO> comments;

  private String description;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime createAt;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime updateAt;

  public TableResponseDTO() {
    super();
  }

  public TableResponseDTO(TableE table) {
    copyProperties(table);
  }

  public void copyProperties(TableE table) {
    BeanUtils.copyProperties(table, this);
    this.createAt = DateUtils.convertToLocalDateTime(table.getCreateAt());
    this.updateAt = DateUtils.convertToLocalDateTime(table.getUpdateAt());
  }
}