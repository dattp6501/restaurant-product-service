package com.dattp.productservice.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.dattp.productservice.dto.dish.DishCreateRequestDTO;
import com.dattp.productservice.dto.dish.DishUpdateRequestDTO;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;

@Entity
@Table(name="DISH")
@Getter
@Setter
public class Dish implements Serializable {
    @Column(name="state")
    @Enumerated(EnumType.STRING)
    private DishState state;

    @Column(name = "id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name="image")
    private String image;

    @Column(name="description")
    private String description;

    @Column(name = "create_at")
    private Long createAt;

    @Column(name = "update_at")
    private Long updateAt;
    
    @OneToMany(mappedBy="dish")
    @Lazy @JsonIgnore
    private List<CommentDish> CommentDishs;

    public Dish() {
    }

    public Dish(DishCreateRequestDTO dishReq) {
        copyProperties(dishReq);
        this.state = DishState.ACTIVE;
    }

    public void copyProperties(DishCreateRequestDTO dishReq){
        BeanUtils.copyProperties(dishReq, this);
        this.createAt = DateUtils.getCurrentMils();
        this.updateAt = DateUtils.getCurrentMils();
    }

    public Dish(DishUpdateRequestDTO dishReq) {
        copyProperties(dishReq);
    }
    public void copyProperties(DishUpdateRequestDTO dishRequestDTO){
        BeanUtils.copyProperties(dishRequestDTO, this);
        this.updateAt = DateUtils.getCurrentMils();
    }
}