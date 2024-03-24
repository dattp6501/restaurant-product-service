package com.dattp.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dattp.productservice.entity.CommentTable;

public interface CommentTableRepository extends JpaRepository<CommentTable,Long>{
    @Query(
        value="SELECT * FROM comment_table ct "
        +"WHERE ct.table_id=:table_id AND ct.user_id=:user_id "
        , nativeQuery=true
    )
    public CommentTable findByTableIdAndUserId(@Param("table_id") Long tableId, @Param("user_id") Long userId);

    @Modifying
    @Query(
        value = "INSERT INTO comment_table(star,comment,table_id,user_id,username, date_) "
        +"VALUES(:star,:comment,:table_id,:user_id,:username,:date_) ",
        nativeQuery=true
    )
    public int save(@Param("star") int star, @Param("comment") String comment, @Param("table_id") Long tableId, @Param("user_id") Long userId, @Param("username") String username, @Param("date_") Long date);

    @Modifying
    @Query(
        value="UPDATE comment_table ct "
        +"SET ct.star=:star, ct.comment=:comment, ct.date_=:date_ "
        +"WHERE ct.table_id=:table_id AND ct.user_id=:user_id "
        , nativeQuery = true
    )
    public int update(@Param("star") int star, @Param("comment") String comment, @Param("table_id") Long tableId, @Param("user_id") Long userId, @Param("date_") Long date);


}
