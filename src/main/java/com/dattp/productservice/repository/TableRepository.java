package com.dattp.productservice.repository;

import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.state.TableState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableRepository extends JpaRepository<TableE, Long> {
  Slice<TableE> findAllByStateIn(List<TableState> states, Pageable pageable);

  Slice<TableE> findAllBy(Pageable pageable);

  List<TableE> findAllByIdInAndStateIn(List<Long> ids, List<TableState> states);

  @Query(
      value = "SELECT * FROM product.TABLE_ t WHERE t.id NOT IN :id",
      nativeQuery = true
  )
  Page<TableE> findAllNotIn(@Param("id") List<Long> list, Pageable pageable);
}