package com.dattp.productservice.controller;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getDishs(Pageable pageable){//page=?&size=?
        return ResponseEntity.ok(
            new ResponseDTO(
                HttpStatus.OK.value(),
                "Thành công",
                dishService.getDishsOverview(pageable)
            )
        );
    }

    @GetMapping(value = "/hot", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getDishsHot(Pageable pageable){//page=?&size=?
        return ResponseEntity.ok(
            new ResponseDTO(
                HttpStatus.OK.value(),
                "Thành công",
                dishService.getDishsOverview(pageable).subList(1, 10)
            )
        );
    }

    @GetMapping(value = "{dish_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO> getDishDetail(@PathVariable("dish_id") long id){
        return ResponseEntity.ok(
          new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            dishService.getDetailFromCache(id)
          )
        );
    }

    @PostMapping(value = "comment", produces = {MediaType.APPLICATION_JSON_VALUE})
    @AddAuthorizedDocAPI
    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
    public ResponseEntity<ResponseDTO> addComment(@RequestBody @Valid CommentDishRequestDTO CDR) throws Exception{
        if(!dishService.addComment(new CommentDish(CDR))) throw new Exception("Không đánh giá được sản phẩm");
        return ResponseEntity.ok(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                null
            )
        );
    }

    @GetMapping(value = "/{dishId}/comment", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getComment(@PathVariable("dishId") Long dishId, Pageable pageable){
        return ResponseEntity.ok().body(
          new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            dishService.getListComment(dishId, pageable)
          )
        );
    }
}