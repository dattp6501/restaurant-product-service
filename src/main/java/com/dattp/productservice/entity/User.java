package com.dattp.productservice.entity;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

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