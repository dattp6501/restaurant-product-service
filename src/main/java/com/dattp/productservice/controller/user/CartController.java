package com.dattp.productservice.controller.user;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.controller.Controller;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.controller.user.dto.DishInCartRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/product/user/cart")
@CrossOrigin(origins = "*")
public class CartController extends Controller {
  @PostMapping(value = "/dish", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
  public ResponseEntity<ResponseDTO> addDishToCart(@RequestBody @Valid DishInCartRequestDTO dto) throws Exception {
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
  @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
  @AddAuthorizedDocAPI
  public ResponseEntity<ResponseDTO> getListDishInCart() {
    return ResponseEntity.ok(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            cartService.getListDish()
        )
    );
  }

  @DeleteMapping(value = "/dish/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
  @AddAuthorizedDocAPI
  public ResponseEntity<ResponseDTO> deleteDish(@PathVariable("id") Long dishId) {
    cartService.deleteDish(dishId);
    return ResponseEntity.ok(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            null
        )
    );
  }

//  @PostMapping(value = "/table", produces = {MediaType.APPLICATION_JSON_VALUE})
//  @AddAuthorizedDocAPI
//  @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
//  public ResponseEntity<ResponseDTO> addTableToCart(@RequestBody @Valid TableInCartRequestDTO dto) throws Exception{
//    cartService.addTableToCart(dto);
//    return ResponseEntity.ok(
//        new ResponseDTO(
//            HttpStatus.OK.value(),
//            "Thành công",
//            null
//        )
//    );
//  }


//  @GetMapping(value = "/table", produces = {MediaType.APPLICATION_JSON_VALUE})
//  @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
//  @AddAuthorizedDocAPI
//  public ResponseEntity<ResponseDTO> getListTableInCart(){
//    return ResponseEntity.ok(
//        new ResponseDTO(
//            HttpStatus.OK.value(),
//            "Thành công",
//            cartService.getListTable()
//        )
//    );
//  }
}