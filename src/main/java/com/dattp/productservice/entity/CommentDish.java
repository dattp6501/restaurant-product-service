package com.dattp.productservice.entity;

import com.dattp.productservice.controller.user.dto.CommentDishRequestDTO;
import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "COMMENT_DISH", schema = "product")
@Getter
@Setter
public class CommentDish implements Serializable {
  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "star", nullable = false)
  private int star;

  @Column(name = "comment", columnDefinition = "text")
  private String comment;

  @Column(name = "date_")
  private Long date;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "id", column = @Column(name = "user_id")),
      @AttributeOverride(name = "username", column = @Column(name = "username"))
  })
  private User user;

  @ManyToOne
  @JoinColumn(name = "dish_id")
  @JsonIgnore
  @Lazy
  private Dish dish;

  @Column(name = "create_at")
  private Long createAt;

  @Column(name = "update_at")
  private Long updateAt;

  public CommentDish() {
  }

  public CommentDish(CommentDishRequestDTO CDR) {
    copyProperties(CDR);
  }

  @PrePersist
  protected void onCreate() {
    this.createAt = this.updateAt = DateUtils.getCurrentMils();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateAt = DateUtils.getCurrentMils();
  }

  public void copyProperties(CommentDishRequestDTO CDR) {
    BeanUtils.copyProperties(CDR, this);
    this.date = DateUtils.getCurrentMils();
    this.dish = new Dish();
    this.dish.setId(CDR.getDishId());
  }
}