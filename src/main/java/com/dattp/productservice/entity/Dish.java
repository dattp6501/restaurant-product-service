package com.dattp.productservice.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="DISH")
@Getter
@Setter
public class Dish {
    @Column(name="state")
    private int state;

    @Column(name = "id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name = "price")
    private float price;

    @Column(name="image")
    private byte[] image;

    @Column(name="description")
    private String description;
    
    @OneToMany(mappedBy="dish")
    private List<CommentDish> CommentDishs;

    public Dish(long id, String name, float price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Dish() {
    }
}