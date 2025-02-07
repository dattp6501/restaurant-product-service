package com.dattp.productservice.controller.manager;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.controller.Controller;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.controller.user.dto.TableCreateRequestDTO;
import com.dattp.productservice.controller.user.dto.TableUpdateRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PRODUCT_UPDATE", "ROLE_PRODUCT_DELETE"})
  public ResponseEntity<ResponseDTO> getAllTable(Pageable pageable) {
    return ResponseEntity.ok().body(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            tableService.getAllFromDB(pageable)
        )
    );
  }

  /*
   * get table detail
   * */
  @GetMapping(value = "/{table_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PRODUCT_UPDATE", "ROLE_PRODUCT_DELETE"})
  public ResponseEntity<ResponseDTO> getTableDetail(@PathVariable("table_id") Long id) {
    return ResponseEntity.ok().body(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            tableService.getDetailDB(id)
        )
    );
  }

  /*
   * create table
   * */
  @PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PRODUCT_NEW"})
  public ResponseEntity<ResponseDTO> create(@RequestBody @Valid TableCreateRequestDTO tableReq) {
    return ResponseEntity.ok().body(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            tableService.create(tableReq)
        )
    );
  }

  /*
   * create table with excel
   * */
  @PostMapping(value = "/save_with_excel", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PRODUCT_NEW"})
  public ResponseEntity<ResponseDTO> save(@RequestParam("file") MultipartFile file) throws IOException {
    return ResponseEntity.ok().body(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            tableService.createByExcel(file.getInputStream())
        )
    );
  }

  @PostMapping(value = "/update_table", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PRODUCT_UPDATE"})
  public ResponseEntity<ResponseDTO> updateTable(@RequestBody @Valid TableUpdateRequestDTO dto) {
    return ResponseEntity.ok().body(
        new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            tableService.update(dto)
        )
    );
  }

}
