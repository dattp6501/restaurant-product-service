package com.dattp.productservice.dto.dish;

import java.time.LocalDateTime;
import java.util.List;

import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class DishResponseDTO {
    private Long id;

    private DishState state;

    private String name;

    private Float price;

    private String image;

    private List<CommentDishResponseDTO> comments;

    private String description;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updateAt;

    public DishResponseDTO() {
    }

    public DishResponseDTO(Dish dish) {
        copyProperties(dish);
    }

    public void copyProperties(Dish dish){
        BeanUtils.copyProperties(dish, this);
        if(dish.getCreateAt() != null) this.createAt = DateUtils.convertToLocalDateTime(dish.getCreateAt());
        if(dish.getUpdateAt() != null) this.updateAt = DateUtils.convertToLocalDateTime(dish.getUpdateAt());
    }
}