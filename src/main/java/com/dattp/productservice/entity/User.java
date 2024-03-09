package com.dattp.productservice.entity;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;


@Embeddable
@Getter
@Setter
public class User {
    private Long id;
    private String username;
    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }
    public User() {
    }
}