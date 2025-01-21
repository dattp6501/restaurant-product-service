package com.dattp.productservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Getter
@Setter
public class User implements Serializable {
  private Long id;
  private String username;

  public User(Long id, String username) {
    this.id = id;
    this.username = username;
  }

  public User() {
  }
}