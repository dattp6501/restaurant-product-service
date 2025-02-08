package com.dattp.productservice.entity;

import com.dattp.productservice.controller.user.dto.TableCreateRequestDTO;
import com.dattp.productservice.controller.user.dto.TableUpdateRequestDTO;
import com.dattp.productservice.entity.state.TableState;
import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TABLE_", schema = "product")
@Getter
@Setter
public class TableE implements Serializable {
  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private TableState state;

  @Column(name = "name", unique = true)
  private String name;

  @Column(name = "image")
  private String image;

  @Column(name = "amount_of_people", nullable = false)
  private Integer amountOfPeople;

  @Column(name = "price")
  private Float price;

  @Column(name = "from_", columnDefinition = "TIME")
  @JsonFormat(pattern = "HH:mm")
  private Date from;

  @Column(name = "to_", columnDefinition = "TIME")
  @JsonFormat(pattern = "HH:mm")
  private Date to;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Column(name = "create_at")
  private Long createAt;

  @Column(name = "update_at")
  private Long updateAt;

  @OneToMany(mappedBy = "table")
  @Lazy
  @JsonIgnore
  private List<CommentTable> CommentTables;

  public TableE() {
  }

  public TableE(TableCreateRequestDTO tableReq) {
    copyProperties(tableReq);
  }

  public TableE(TableUpdateRequestDTO tableReq) {
    copyProperties(tableReq);
  }

  public static List<TableState> getListStatusReady() {
    return List.of(TableState.ACTIVE, TableState.NEW);
  }

  public static List<TableState> getListStatusNotReady() {
    return List.of(TableState.INACTIVE, TableState.DELETE);
  }

  @PrePersist
  protected void onCreate() {
    this.createAt = this.updateAt = DateUtils.getCurrentMils();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateAt = DateUtils.getCurrentMils();
  }

  public void copyProperties(TableCreateRequestDTO tableReq) {
    BeanUtils.copyProperties(tableReq, this);
    this.createAt = DateUtils.getCurrentMils();
    this.updateAt = DateUtils.getCurrentMils();
    this.state = TableState.ACTIVE;
  }

  public void copyProperties(TableUpdateRequestDTO tableReq) {
    BeanUtils.copyProperties(tableReq, this);
    this.updateAt = DateUtils.getCurrentMils();
  }
}