package com.dattp.productservice.controller;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.dto.dish.DishInCartRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/product/user/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController extends Controller{
  @PostMapping(value = "/dish", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
  public ResponseEntity<ResponseDTO> addDishToCart(@RequestBody @Valid DishInCartRequestDTO dto) throws Exception{
    cartService.addDishToCart(dto);
    return ResponseEntity.ok().body(
      new ResponseDTO(
        HttpStatus.OK.value(),
        "Thành công",
        null
      )
    );
  }

  
  @GetMapping(value = "/dish", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  public ResponseEntity<ResponseDTO> getListDishInCart(){
    return ResponseEntity.ok(
      new ResponseDTO(
        HttpStatus.OK.value(),
        "Thành công",
        cartService.getListDish()
      )
    );
  }
}