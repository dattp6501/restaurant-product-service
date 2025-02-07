package com.dattp.productservice.controller.user;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.controller.Controller;
import com.dattp.productservice.controller.user.dto.CommentDishRequestDTO;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.pojo.DishOverview;
import com.dattp.productservice.response.PageSliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/product/user/dish")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DishControllerUser extends Controller {
  @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<PageSliceResponse<DishOverview>> findPageDish(Pageable pageable) {//page=?&size=?
    return ResponseEntity.ok(
        dishService.findPageDish(pageable)
    );
  }

  @GetMapping(value = "/hot", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseDTO> getDishsHot(Pageable pageable) {//page=?&size=?
    return ResponseEntity.ok(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            dishService.getDishsHot(pageable)
        )
    );
  }

  @GetMapping(value = "{dish_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseDTO> getDishDetail(@PathVariable("dish_id") long id) {
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
  public ResponseEntity<ResponseDTO> addComment(@RequestBody @Valid CommentDishRequestDTO CDR) throws Exception {
    if (!dishService.addComment(new CommentDish(CDR))) throw new Exception("Không đánh giá được sản phẩm");
    return ResponseEntity.ok(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            null
        )
    );
  }

  @GetMapping(value = "/{dishId}/comment", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> getComment(@PathVariable("dishId") Long dishId, Pageable pageable) {
    return ResponseEntity.ok().body(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            dishService.getListComment(dishId, pageable)
        )
    );
  }
}