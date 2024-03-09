package com.dattp.productservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dattp.productservice.dto.DishRequestDTO;
import com.dattp.productservice.dto.DishResponseDTO;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.service.DishService;

@RestController
@RequestMapping("/api/product/manage/dish")
public class DishControllerManager {
    @Autowired
    private DishService dishService;

    @PostMapping("/save")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_NEW"})
    public ResponseEntity<ResponseDTO> save(@RequestBody @Valid DishRequestDTO dishR){
        Dish dish = new Dish();
        dish.setName(dishR.getName());
        dish.setPrice(dishR.getPrice());
        dish.setDescription(dishR.getDescription());
        dish = dishService.save(dish);
        DishResponseDTO dishDTO = new DishResponseDTO();
        BeanUtils.copyProperties(dish, dishDTO);
        return ResponseEntity.ok().body(
            new ResponseDTO(HttpStatus.OK.value(),
            "Thành công", 
            dishDTO)
        );
    }

    @PostMapping("/save_with_excel")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_NEW"})
    public ResponseEntity<ResponseDTO> save(@RequestParam("file") MultipartFile file) throws IOException{
        List<Dish> dishs = dishService.save(dishService.readXlsxDish(file.getInputStream()));
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(),
                "Thành công",
                dishs
            )
        );
    }

    @GetMapping("/get_dish")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_UPDATE","ROLE_PRODUCT_DELETE"})
    public ResponseEntity<ResponseDTO> getDishs(Pageable pageable){//page=?&size=?
        List<DishResponseDTO> list = new ArrayList<>();
        dishService.getDishs(pageable).getContent().forEach((d)->{
            DishResponseDTO dishResp = new DishResponseDTO();
            BeanUtils.copyProperties(d, dishResp);
            list.add(dishResp);
        });
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công",
                list
            )
        );
    }

    @GetMapping
    @RequestMapping("/get_dish_detail/{dish_id}")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_UPDATE","ROLE_PRODUCT_DELETE"})
    public ResponseEntity<ResponseDTO> getDishDetail(@PathVariable("dish_id") long id){
        DishResponseDTO dishResp = new DishResponseDTO();
        Dish dish = dishService.getById(id);
        BeanUtils.copyProperties(dish, dishResp);
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                dishResp
            )
        );
    }

    @PostMapping
    @RequestMapping("/update_dish")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_UPDATE"})
    public ResponseEntity<ResponseDTO> updateDish(@RequestBody @Valid DishRequestDTO dishRequestDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishRequestDTO, dish);
        dishService.save(dish);
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(),
                "Thành công",
                null
            )
        );
    }
}
