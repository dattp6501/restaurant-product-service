package com.dattp.productservice.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dattp.productservice.dto.CommentTableRequestDTO;
import com.dattp.productservice.dto.CommentTableResponseDTO;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.dto.TableResponseDTO;
import com.dattp.productservice.entity.CommentTable;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.service.TableService;

@RestController
@RequestMapping("/api/product/user/table")
public class TableControllerUser {
    @Autowired
    private TableService tableService;

    @GetMapping(value = "/get_table")
    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
    public ResponseEntity<ResponseDTO> getAllTable(Pageable pageable){
        List<TableResponseDTO> list = new ArrayList<>();
        tableService.getAll(pageable).getContent().forEach((t)->{
            TableResponseDTO tableResp = new TableResponseDTO();
            BeanUtils.copyProperties(t, tableResp);
            list.add(tableResp);
        });
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                list
            )
        );
    }

    @GetMapping
    @RequestMapping("/get_table_freetime")
    public ResponseEntity<ResponseDTO> getTableFreeTime(@RequestParam("from") @DateTimeFormat(
        pattern="HH:mm:ss dd/MM/yyyy") Date from, @RequestParam("to") @DateTimeFormat(pattern="HH:mm:ss dd/MM/yyyy") Date to, 
        Pageable pageable, @RequestHeader(value="access_token", required=false) String accessToken
    ){
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
            "Thành công", 
                tableService.getFreeTimeOfTable(from, to, pageable, accessToken)
            )
        );
    }

    @GetMapping
    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
    @RequestMapping("/get_table_detail/{table_id}")
    public ResponseEntity<ResponseDTO> getTableDetail(@PathVariable("table_id") long id){
        TableResponseDTO tableResp = new TableResponseDTO();
        TableE table = tableService.getById(id);
        BeanUtils.copyProperties(table, tableResp);
        if(!table.getCommentTables().isEmpty()){
            tableResp.setComments(new ArrayList<>());
            table.getCommentTables().stream().forEach((c)->{
                CommentTableResponseDTO cr = new CommentTableResponseDTO();
                BeanUtils.copyProperties(c, cr);
                cr.setUserId(c.getUser().getId());
                cr.setUsername(c.getUser().getUsername());
                tableResp.getComments().add(cr);
            });
        }
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                tableResp
            )
        );
    }

    @PostMapping
    @RequestMapping("add_comment")
    @RolesAllowed({"ROLE_PRODUCT_ACCESS"})
    public ResponseEntity<ResponseDTO> addComment(@RequestBody @Valid CommentTableRequestDTO CTR) throws Exception{
        CommentTable CT = new CommentTable();
        BeanUtils.copyProperties(CTR, CT);
        CT.setUser(new User(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), null));
        if(!tableService.addComment(CTR.getTableId(), CT)) throw new Exception("Không đánh giá được sản phẩm");
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                null
            )
        );
    }
}