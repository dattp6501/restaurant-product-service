package com.dattp.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dattp.productservice.entity.CommentTable;

public interface CommentTableRepository extends JpaRepository<CommentTable,Long>{
    @Query(
        value="SELECT * FROM COMMENT_TABLE ct "
        +"WHERE ct.table_id=:table_id AND ct.user_id=:user_id "
        , nativeQuery=true
    )
    public CommentTable findByTableIdAndUserId(@Param("table_id") Long tableId, @Param("user_id") Long userId);

    @Modifying
    @Query(
        value = "INSERT INTO COMMENT_TABLE(star,comment,table_id,user_id,username) "
        +"VALUES(:star,:comment,:table_id,:user_id,:username) ", 
        nativeQuery=true
    )
    public int save(@Param("star") int star, @Param("comment") String comment, @Param("table_id") Long tableId, @Param("user_id") Long userId, @Param("username") String username);

    @Modifying
    @Query(
        value="UPDATE COMMENT_TABLE ct "
        +"SET ct.star=:star, ct.comment=:comment "
        +"WHERE ct.table_id=:table_id AND ct.user_id=:user_id "
        , nativeQuery = true
    )
    public int update(@Param("star") int star, @Param("comment") String comment, @Param("table_id") Long tableId, @Param("user_id") Long userId);


}
