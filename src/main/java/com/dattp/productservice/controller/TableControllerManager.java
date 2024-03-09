package com.dattp.productservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dattp.productservice.config.ApplicationConfig;
import com.dattp.productservice.dto.CommentTableResponseDTO;
import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.dto.TableRequestDTO;
import com.dattp.productservice.dto.TableResponseDTO;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.service.TableService;

@RestController
@RequestMapping("/api/product/manage/table")
public class TableControllerManager {
    @Autowired
    private TableService tableService;

    @PostMapping(value = "/save")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_NEW"})
    public ResponseEntity<ResponseDTO> save(@RequestBody @Valid TableRequestDTO tableR){
        TableE table = new TableE();
        table.setState(ApplicationConfig.DEFAULT_STATE);
        table.setName(tableR.getName());
        table.setAmountOfPeople(tableR.getAmountOfPeople());
        table.setPrice(tableR.getPrice());
        table.setDescription(tableR.getDescription());
        table = tableService.saveTable(table);
        TableResponseDTO tableDTO = new TableResponseDTO();
        BeanUtils.copyProperties(table, tableDTO);
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                tableDTO
            )
        );
    }
    
    @PostMapping(value = "/save_with_excel")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_NEW"})
    public ResponseEntity<ResponseDTO> save(@RequestParam("file")MultipartFile file) throws IOException{
        List<TableE> newList = tableService.save(tableService.readXlsxTable(file.getInputStream()));
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                newList
            )
        );
    }

    @GetMapping(value = "/get_table")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_UPDATE","ROLE_PRODUCT_DELETE"})
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
    @RolesAllowed({"ROLE_PRODUCT_UPDATE","ROLE_PRODUCT_DELETE"})
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

    @PostMapping(value = "/update_table")
    @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT_UPDATE"})
    public ResponseEntity<ResponseDTO> updateTable(@RequestBody @Valid TableRequestDTO tableRequestDTO){
        TableE table = new TableE();
        BeanUtils.copyProperties(tableRequestDTO, table);
        tableService.saveTable(table);
        return ResponseEntity.ok().body(
            new ResponseDTO(
                HttpStatus.OK.value(), 
                "Thành công", 
                null
            )
        );
    }

}
