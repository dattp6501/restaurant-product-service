package com.dattp.productservice.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dattp.productservice.dto.table.CommentTableRequestDTO;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.entity.CommentTable;

@RestController
@RequestMapping("/api/product/user/table")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TableControllerUser extends Controller{
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
    public ResponseEntity<ResponseDTO> getAllTable(Pageable pageable){
        return ResponseEntity.ok(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                tableService.getTableOverview(pageable)
            )
        );
    }
    @GetMapping(value = "/{table_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
    public ResponseEntity<ResponseDTO> getTableDetail(@PathVariable("table_id") Long id){
        return ResponseEntity.ok(
          new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            tableService.getDetailFromCache(id)
          )
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
    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
    public ResponseEntity<ResponseDTO> addComment(@RequestBody @Valid CommentTableRequestDTO CTR) throws Exception{
        if(!tableService.addComment(new CommentTable(CTR))) throw new Exception("Không đánh giá được sản phẩm");
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                null
            )
        );
    }

    @GetMapping(value = "/{tableId}/comment", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO> addComment(@PathVariable("tableId") Long tableId, Pageable pageable){
        return ResponseEntity.ok().body(
          new ResponseDTO(
            HttpStatus.OK.value(),
            "Thành công",
            tableService.getListCommentTable(tableId, pageable)
          )
        );
    }
}