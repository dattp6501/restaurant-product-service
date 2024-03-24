package com.dattp.productservice.repository;


import com.dattp.productservice.entity.state.DishState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dattp.productservice.entity.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish,Long>{
  Page<Dish> findDishesByStateIn(List<DishState> states, Pageable pageable);
  List<Dish> findAllByStateIn(List<DishState> states);
}