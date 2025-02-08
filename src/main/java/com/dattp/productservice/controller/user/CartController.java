package com.dattp.productservice.controller.user;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.base.ErrorMessage;
import com.dattp.productservice.base.response.BaseResponse;
import com.dattp.productservice.controller.Controller;
import com.dattp.productservice.controller.user.dto.DishInCartRequestDTO;
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
  public ResponseEntity<BaseResponse> addDishToCart(@RequestBody @Valid DishInCartRequestDTO dto) throws Exception {
    cartService.addDishToCart(dto);
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(null)
            .build()
    );
  }


  @GetMapping(value = "/dish", produces = {MediaType.APPLICATION_JSON_VALUE})
  @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
  @AddAuthorizedDocAPI
  public ResponseEntity<BaseResponse> getListDishInCart() {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(cartService.getListDish())
            .build()
    );
  }

  @DeleteMapping(value = "/dish/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
  @AddAuthorizedDocAPI
  public ResponseEntity<BaseResponse> deleteDish(@PathVariable("id") Long dishId) {
    cartService.deleteDish(dishId);
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(null)
            .build()
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