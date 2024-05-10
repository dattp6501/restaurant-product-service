package com.dattp.productservice.controller;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.dto.dish.DishUpdateRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dattp.productservice.dto.dish.DishCreateRequestDTO;
import com.dattp.productservice.dto.ResponseDTO;

@RestController
@RequestMapping("/api/product/manage/dish")
@CrossOrigin(origins = "*")
public class DishControllerManager extends Controller{
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @AddAuthorizedDocAPI
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_UPDATE","ROLE_PRODUCT_DELETE"})
    public ResponseEntity<?> getDishs(Pageable pageable){//page=?&size=?
        return ResponseEntity.ok().body(
          new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            dishService.getDishsFromDB(pageable)
          )
        );
    }

    @GetMapping(value = "/{dish_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @AddAuthorizedDocAPI
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_UPDATE","ROLE_PRODUCT_DELETE"})
    public ResponseEntity<ResponseDTO> getDishDetail(@PathVariable("dish_id") long id){
        return ResponseEntity.ok().body(
          new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            dishService.getDetailFromDB(id)
          )
        );
    }


    @PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})
    @AddAuthorizedDocAPI
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_NEW"})
    public ResponseEntity<?> create(@RequestBody @Valid DishCreateRequestDTO dishReq){
        return ResponseEntity.ok().body(
            new ResponseDTO(HttpStatus.OK.value(),
            "Thành công",
              dishService.create(dishReq)
            )
        );
    }

    @PostMapping(value = "/save_with_excel", produces = {MediaType.APPLICATION_JSON_VALUE})
    @AddAuthorizedDocAPI
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_NEW"})
    public ResponseEntity<?> createByExcel(@RequestParam("file") MultipartFile file) throws IOException{
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(),
                "Thành công",
                dishService.createByExcel(file.getInputStream())
            )
        );
    }


    @PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})
    @AddAuthorizedDocAPI
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_UPDATE"})
    public ResponseEntity<?> updateDish(@RequestBody @Valid DishUpdateRequestDTO dishRequestDTO){
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(),
                "Thành công",
                dishService.update(dishRequestDTO)
            )
        );
    }
}
