package com.dattp.productservice.repository;

import java.util.List;

import com.dattp.productservice.entity.state.TableState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dattp.productservice.entity.TableE;

public interface TableRepository extends JpaRepository<TableE,Long>{
    Page<TableE> findAllByStateIn(List<TableState> states, Pageable pageable);
    List<TableE> findAllByIdInAndStateIn(List<Long> ids, List<TableState> states);

    @Query(
        value = "SELECT * FROM TABLE_ t WHERE t.id NOT IN :id",
        nativeQuery = true
    )
    public Page<TableE> findAllNotIn(@Param("id") List<Long> list, Pageable pageable);
}