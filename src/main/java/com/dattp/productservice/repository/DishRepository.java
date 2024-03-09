package com.dattp.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dattp.productservice.entity.Dish;

public interface DishRepository extends JpaRepository<Dish,Long>{
    
}