package com.dattp.productservice.controller;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dattp.productservice.dto.dish.CommentDishRequestDTO;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.User;

@RestController
@RequestMapping("/api/product/user/dish")
public class DishControllerUser extends Controller{
    @GetMapping("/get_dish")
    public ResponseEntity<?> getDishs(Pageable pageable){//page=?&size=?
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công",
                dishService.getDishs(pageable)
            )
        );
    }

    @PostMapping("add_comment")
    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
    public ResponseEntity<ResponseDTO> addComment(@RequestBody @Valid CommentDishRequestDTO CDR) throws Exception{
        CommentDish CD = new CommentDish();
        BeanUtils.copyProperties(CDR, CD);
        CD.setDate(new Date());
        CD.setUser(new User(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), null));
        if(!dishService.addComment(CDR.getDishId(), CD)) throw new Exception("Không đánh giá được sản phẩm");
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                null
            )
        );
    }

    @GetMapping
    @RequestMapping("/get_dish_detail/{dish_id}")
    public ResponseEntity<ResponseDTO> getDishDetail(@PathVariable("dish_id") long id){
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                dishService.getDetail(id)
            )
        );
    }
}