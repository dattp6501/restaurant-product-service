package com.dattp.productservice.controller.manager;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.base.ErrorMessage;
import com.dattp.productservice.base.response.BaseResponse;
import com.dattp.productservice.controller.Controller;
import com.dattp.productservice.controller.manager.response.TableManagerResponse;
import com.dattp.productservice.controller.user.dto.TableCreateRequestDTO;
import com.dattp.productservice.controller.user.dto.TableUpdateRequestDTO;
import com.dattp.productservice.entity.myenum.SysAction;
import com.dattp.productservice.response.PageSliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/product/manage/table")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TableControllerManager extends Controller {
  /*
   * get tables
   * */
  @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({
      SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_UPDATE, SysAction.ROLE_PRODUCT_DELETE
  })
  public ResponseEntity<PageSliceResponse<TableManagerResponse>> findListTable(Pageable pageable) {
    return ResponseEntity.ok(
        tableService.findListTableManager(pageable)
    );
  }

  /*
   * get table detail
   * */
  @GetMapping(value = "/{table_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({
      SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_UPDATE, SysAction.ROLE_PRODUCT_DELETE
  })
  public ResponseEntity<BaseResponse> getTableDetail(@PathVariable("table_id") Long id) {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(tableService.getDetailManager(id))
            .build()
    );
  }

  /*
   * create table
   * */
  @PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_NEW})
  public ResponseEntity<BaseResponse> create(@RequestBody @Valid TableCreateRequestDTO tableReq) {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(tableService.create(tableReq))
            .build()
    );
  }

  /*
   * create table with excel
   * */
  @PostMapping(value = "/save_with_excel", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_NEW})
  public ResponseEntity<BaseResponse> save(@RequestParam("file") MultipartFile file) throws IOException {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(tableService.createByExcel(file.getInputStream()))
            .build()
    );
  }

  @PostMapping(value = "/update_table", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({SysAction.ROLE_ADMIN, SysAction.ROLE_PRODUCT_UPDATE})
  public ResponseEntity<BaseResponse> updateTable(@RequestBody @Valid TableUpdateRequestDTO dto) {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(tableService.update(dto))
            .build()
    );
  }

}
