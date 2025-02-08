package com.dattp.productservice.controller.user;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import com.dattp.productservice.base.ErrorMessage;
import com.dattp.productservice.base.response.BaseResponse;
import com.dattp.productservice.controller.Controller;
import com.dattp.productservice.controller.user.dto.CommentTableRequestDTO;
import com.dattp.productservice.entity.myenum.SysAction;
import com.dattp.productservice.controller.user.response.TableOverviewResponse;
import com.dattp.productservice.response.PageSliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/product/user/table")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TableControllerUser extends Controller {
  @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
  public ResponseEntity<PageSliceResponse<TableOverviewResponse>> getAllTable(Pageable pageable) {
    return ResponseEntity.ok(
        tableService.getTableOverview(pageable)
    );
  }

  @GetMapping(value = "/{table_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
  public ResponseEntity<BaseResponse> getTableDetail(@PathVariable("table_id") Long id) {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(tableService.getDetailUser(id))
            .build()
    );
  }

//    @GetMapping("/freetime")
//    @AddAuthorizedDocAPI
//    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
//    public ResponseEntity<ResponseDTO> getTableFreeTime(@RequestParam("from") @DateTimeFormat(
//        pattern="HH:mm:ss dd/MM/yyyy") Date from, @RequestParam("to") @DateTimeFormat(pattern="HH:mm:ss dd/MM/yyyy") Date to,
//        Pageable pageable, @RequestHeader(value="access_token", required=false) String accessToken
//    ){
//        return ResponseEntity.ok().body(
//            new ResponseDTO(
//                HttpStatus.OK.value(),
//            "Thành công",
//                tableService.getFreeTimeOfTable(from, to, pageable, accessToken)
//            )
//        );
//    }


  @PostMapping(value = "/comment", produces = {MediaType.APPLICATION_JSON_VALUE})
  @AddAuthorizedDocAPI
  @RolesAllowed({SysAction.ROLE_PRODUCT_ACCESS})
  public ResponseEntity<BaseResponse> addComment(@RequestBody @Valid CommentTableRequestDTO CTR) throws Exception {
    tableService.addComment(CTR);
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(null)
            .build()
    );
  }

  @GetMapping(value = "/{tableId}/comment", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<BaseResponse> addComment(@PathVariable("tableId") Long tableId, Pageable pageable) {
    return ResponseEntity.ok(
        BaseResponse.builder()
            .code(ErrorMessage.SUCCESS.getStatus().value())
            .message(ErrorMessage.SUCCESS.getMessage())
            .data(tableService.getListCommentTable(tableId, pageable))
            .build()
    );
  }
}