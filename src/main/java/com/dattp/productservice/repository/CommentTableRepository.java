package com.dattp.productservice.repository;

import com.dattp.productservice.entity.CommentTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentTableRepository extends JpaRepository<CommentTable, Long> {
  @Query(
      value = "SELECT * FROM product.comment_table ct "
          + "WHERE ct.table_id=:table_id AND ct.user_id=:user_id "
      , nativeQuery = true
  )
  CommentTable findByTableIdAndUserId(@Param("table_id") Long tableId, @Param("user_id") Long userId);

  @Modifying
  @Query(
      value = "INSERT INTO product.comment_table(star,comment,table_id,user_id,username, date_) "
          + "VALUES(:star,:comment,:table_id,:user_id,:username,:date_) ",
      nativeQuery = true
  )
  int save(@Param("star") int star, @Param("comment") String comment, @Param("table_id") Long tableId, @Param("user_id") Long userId, @Param("username") String username, @Param("date_") Long date);

  @Modifying
  @Query(
      value = "UPDATE product.comment_table ct "
          + "SET ct.star=:star, ct.comment=:comment, ct.date_=:date_ "
          + "WHERE ct.table_id=:table_id AND ct.user_id=:user_id "
      , nativeQuery = true
  )
  int update(@Param("star") int star, @Param("comment") String comment, @Param("table_id") Long tableId, @Param("user_id") Long userId, @Param("date_") Long date);

  Page<CommentTable> findCommentTablesByTableId(Long tableId, Pageable pageable);
}
