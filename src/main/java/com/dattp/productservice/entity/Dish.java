package com.dattp.productservice.entity;

import com.dattp.productservice.controller.user.dto.DishCreateRequestDTO;
import com.dattp.productservice.controller.user.dto.DishUpdateRequestDTO;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.utils.DateUtils;
import com.dattp.productservice.utils.JSONUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "DISH", schema = "product")
@Getter
@Setter
public class Dish implements Serializable {
  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private DishState state;

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "price")
  private Float price;

  @Column(name = "image")
  private String image;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Column(name = "create_at")
  private Long createAt;

  @Column(name = "update_at")
  private Long updateAt;

  @OneToMany(mappedBy = "dish")
  @Lazy
  @JsonIgnore
  private List<CommentDish> CommentDishs;

  public Dish() {
  }

  public Dish(DishCreateRequestDTO dishReq) {
    copyProperties(dishReq);
    this.state = DishState.ACTIVE;
  }

  public Dish(DishUpdateRequestDTO dishReq) {
    copyProperties(dishReq);
  }

  @PrePersist
  protected void onCreate() {
    this.createAt = this.updateAt = DateUtils.getCurrentMils();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateAt = DateUtils.getCurrentMils();
  }

  public void copyProperties(DishCreateRequestDTO dishReq) {
    BeanUtils.copyProperties(dishReq, this);
    this.createAt = DateUtils.getCurrentMils();
    this.updateAt = DateUtils.getCurrentMils();
  }

  public void copyProperties(DishUpdateRequestDTO dishRequestDTO) {
    BeanUtils.copyProperties(dishRequestDTO, this);
    this.updateAt = DateUtils.getCurrentMils();
  }

  @Override
  public String toString() {
    return JSONUtils.toJson(this);
  }
}