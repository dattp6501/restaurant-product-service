package com.dattp.productservice.controller.user;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.base.ErrorMessage;
import com.dattp.productservice.base.response.BaseResponse;
import com.dattp.productservice.base.response.dish.DishOverviewResponse;
import com.dattp.productservice.controller.Controller;
import com.dattp.productservice.controller.user.dto.CommentDishRequestDTO;
import com.dattp.productservice.entity.myenum.SysAction;
import com.dattp.productservice.response.PageSliceResponse;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<PageSliceResponse<DishOverviewResponse>> findPageDish(Pageable pageable) {//page=?&size=?
        return ResponseEntity.ok(
                dishService.findPageDish(pageable)
        );
    }

    @GetMapping(value = "/hot", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PageSliceResponse<DishOverviewResponse>> getListDishHot(Pageable pageable) {//page=?&size=?
        return ResponseEntity.ok(
                dishService.getListDishHot(pageable)
        );
    }

    @GetMapping(value = "{dish_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> getDishDetail(@PathVariable("dish_id") long id) {
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .code(ErrorMessage.SUCCESS.getStatus().value())
                        .message(ErrorMessage.SUCCESS.getMessage())
                        .data(dishService.getDetail(id))
                        .build()
        );
    }

    @PostMapping(value = "comment", produces = {MediaType.APPLICATION_JSON_VALUE})
    @AddAuthorizedDocAPI
    @RolesAllowed({
            SysAction.ROLE_PRODUCT_ACCESS
    })
    public ResponseEntity<BaseResponse> addComment(@RequestBody @Valid CommentDishRequestDTO CDR) throws Exception {
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .code(ErrorMessage.SUCCESS.getStatus().value())
                        .message(ErrorMessage.SUCCESS.getMessage())
                        .data(dishService.addComment(CDR))
                        .build()
        );
    }

    @GetMapping(value = "/{dishId}/comment", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getComment(@PathVariable("dishId") Long dishId, Pageable pageable) {
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .code(ErrorMessage.SUCCESS.getStatus().value())
                        .message(ErrorMessage.SUCCESS.getMessage())
                        .data(dishService.getListComment(dishId, pageable))
                        .build()
        );
    }
}