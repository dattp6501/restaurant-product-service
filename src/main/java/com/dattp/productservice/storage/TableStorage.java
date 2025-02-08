package com.dattp.productservice.storage;

import com.dattp.productservice.entity.TableE;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class TableStorage extends Storage {
  //================================  LIST TABLE =========================================
  /*
   * user
   * */


  /*
   * admin
   * */

  public List<TableE> findAllTableReadyByIdInAndStateIn(List<Long> ids) {
    return tableRepository.findAllByIdInAndStateIn(ids, TableE.getListStatusReady());
  }

  //================================ TABLE DETAIL =======================================
  /*
   * user
   * */

  /*
   * admin
   * */

  //=================================== SAVE TABLE ========================


  //=======================================    COMMENT  =============================================
}
