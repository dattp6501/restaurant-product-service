package com.dattp.productservice.dto.kafka;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookedDishRequestKafkaDTO {
    private int state;
    private long id;
    private long dishId;
    private String name;
    private int total;
    private float price;
    public BookedDishRequestKafkaDTO(long id, long dishId, String name, int total, float price) {
        this.id = id;
        this.dishId = dishId;
        this.name = name;
        this.total = total;
        this.price = price;
    }
    public BookedDishRequestKafkaDTO() {
    }
}
