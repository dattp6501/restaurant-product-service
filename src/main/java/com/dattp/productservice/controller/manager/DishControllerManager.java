package com.dattp.productservice.controller.manager;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.base.ErrorMessage;
import com.dattp.productservice.base.response.BaseResponse;
import com.dattp.productservice.controller.Controller;
import com.dattp.productservice.controller.user.dto.DishCreateRequestDTO;
import com.dattp.productservice.controller.user.dto.DishUpdateRequestDTO;
import com.dattp.productservice.entity.myenum.SysAction;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/product/manage/dish")
@CrossOrigin(origins = "*")
public class DishControllerManager extends Controller {
  @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({
      SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_UPDATE, SysAction.ROLE_PRODUCT_DELETE
  })
  public ResponseEntity<?> getListDishManager(Pageable pageable) {//page=?&size=?
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(dishService.getListDishManager(pageable))
            .build()
    );
  }

  @GetMapping(value = "/{dish_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({
      SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_UPDATE, SysAction.ROLE_PRODUCT_DELETE
  })
  public ResponseEntity<BaseResponse> getDishDetail(@PathVariable("dish_id") long id) {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(dishService.getDetailManager(id))
            .build()
    );
  }


  @PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({
      SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_NEW
  })
  public ResponseEntity<?> create(@RequestBody @Valid DishCreateRequestDTO dishReq) {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(dishService.create(dishReq))
            .build()
    );
  }

  @PostMapping(value = "/save_with_excel", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({
      SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_NEW
  })
  public ResponseEntity<?> createByExcel(@RequestParam("file") MultipartFile file) throws IOException {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(dishService.createByExcel(file.getInputStream()))
            .build()
    );
  }


  @PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({
      SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_UPDATE
  })
  public ResponseEntity<?> updateDish(@RequestBody @Valid DishUpdateRequestDTO dishRequestDTO) {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(dishService.update(dishRequestDTO))
            .build()
    );
  }
}
