package com.dattp.productservice.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dattp.productservice.entity.CommentDish;

public interface CommentDishRepository extends JpaRepository<CommentDish,Long>{
    @Query(
        value="SELECT * FROM comment_dish cd "
        +"WHERE cd.dish_id=:dish_id AND cd.user_id=:user_id "
        , nativeQuery=true
    )
    public CommentDish findByDishIdAndUserId(@Param("dish_id") Long dishId, @Param("user_id") Long userId);

    @Modifying
    @Query(
        value = "INSERT INTO comment_dish(star,comment,dish_id,user_id,username,date_) "
        +"VALUES(:star,:comment,:dish_id,:user_id,:username,:date) ", 
        nativeQuery=true
    )
    public int save(@Param("star") int star, @Param("comment") String comment, @Param("dish_id") Long dishId, @Param("user_id") Long userId, @Param("username") String username, @Param("date") Long date);

    @Modifying
    @Query(
        value="UPDATE comment_dish cd "
        +"SET cd.star=:star, cd.comment=:comment, cd.date_=:date "
        +"WHERE cd.dish_id=:dish_id AND cd.user_id=:user_id "
        , nativeQuery = true
    )
    public int update(@Param("star") int star, @Param("comment") String comment, @Param("dish_id") Long dishId, @Param("user_id") Long userId, @Param("date") Long date);

    public List<CommentDish> findCommentDishesByDish_Id(Long dishId,  Pageable pageable);
}