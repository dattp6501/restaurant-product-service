package com.dattp.productservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.dattp.productservice.dto.table.CommentTableResponseDTO;
import com.dattp.productservice.dto.table.TableCreateRequestDTO;
import com.dattp.productservice.dto.table.TableResponseDTO;
import com.dattp.productservice.dto.table.TableUpdateRequestDTO;
import com.dattp.productservice.entity.state.TableState;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dattp.productservice.dto.ResponseDTO;
import com.dattp.productservice.dto.resttemplate.PeriodTimeResponseDTO;
import com.dattp.productservice.dto.resttemplate.PeriodsTimeBookedTableDTO;
import com.dattp.productservice.dto.resttemplate.ResponseListTableFreeTimeDTO;
import com.dattp.productservice.entity.CommentTable;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.exception.BadRequestException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TableService extends com.dattp.productservice.service.Service {
    /*
    * get list table
    * */
    public List<TableResponseDTO> getAll(Pageable pageable){
        List<TableResponseDTO> list = new ArrayList<>();
        tableRepository.findAll(pageable).getContent().forEach((t)->{
            TableResponseDTO tableResp = new TableResponseDTO(t);
            list.add(tableResp);
        });
        return list;
    }
    /*
    * get table detail
    * */
    public TableResponseDTO getDetail(Long id){
        TableE tableE = tableRepository.findById(id).orElseThrow();
        TableResponseDTO tableResp = new TableResponseDTO(tableE);
        return tableResp;
    }
    /*
    * create table
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public TableResponseDTO create(TableCreateRequestDTO tableReq) {
        TableE table = new TableE(tableReq);
        table = tableRepository.save(table);
      return new TableResponseDTO(table);
    }
    /*
    * create table with excel
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Boolean createByExcel(InputStream inputStream) throws IOException {
        List<TableE> tables = readXlsxTable(inputStream);
        tableRepository.saveAll(tables);
        return true;
    }
    public List<TableE> readXlsxTable(InputStream inputStream) throws IOException{
        List<TableE> tables = new ArrayList<>();
        final int COLUMN_INDEX_NAME = 0;
        final int COLUMN_INDEX_AMOUNT_OF_PEOPLE = 1;
        final int COLUMN_INDEX_PRICE = 2;
        final int COLUMN_INDEX_FROM = 3;
        final int COLUMN_INDEX_TO = 4;
        final int COLUMN_INDEX_DESCRIPTION = 5;
        // xlsx: XSSFWorkbook, xls: HSSFWorkbook,
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        it.next();//bo qua dong dong tien(dong tieu de)
        int index = 0;
        while(it.hasNext()) {
            index++;
            boolean isRowEmpty = true;
            Row row = it.next();
            TableE table = new TableE();
            table.setState(TableState.ACTIVE);
            for(int i=0; i<6; i++){
                if(i==COLUMN_INDEX_NAME){
                    if(row.getCell(i)!=null && !row.getCell(i).getStringCellValue().equals("")) {
                        table.setName(row.getCell(i).getStringCellValue());
                        isRowEmpty = false;
                    }
                    continue;
                }
                if(i==COLUMN_INDEX_AMOUNT_OF_PEOPLE){
                    if(row.getCell(i)!=null){
                        table.setAmountOfPeople((int)row.getCell(i).getNumericCellValue());
                        isRowEmpty = false;
                    }
                    continue;
                }
                if(i==COLUMN_INDEX_PRICE){
                    if(row.getCell(i)!=null){
                        table.setPrice((float)row.getCell(i).getNumericCellValue());
                        isRowEmpty = false;
                    }
                    continue;
                }
                if(i==COLUMN_INDEX_FROM){
                    if(row.getCell(i)!=null){
                        table.setFrom(row.getCell(i).getDateCellValue());
                        isRowEmpty = false;
                    }
                    continue;
                }
                if(i==COLUMN_INDEX_TO){
                    if(row.getCell(i)!=null){
                        table.setTo(row.getCell(i).getDateCellValue());
                        isRowEmpty = false;
                    }
                    continue;
                }
                if(i==COLUMN_INDEX_DESCRIPTION){
                    if(row.getCell(i)!=null){
                        table.setDescription(row.getCell(i).getStringCellValue());
                        isRowEmpty = false;
                    }
                    continue;
                }
            }
            if(isRowEmpty) continue;//if row empty
            // row not empty
            if(table.getName()==null || table.getName().equals("")){
                workbook.close();
                throw new BadRequestException("Dòng "+index+": Tên bàn không được để trống");
            }
            if(table.getAmountOfPeople()<=0){
                workbook.close();
                throw new BadRequestException("Dòng "+index+": Số người ngồi phải lớn hơn 0");
            }
            if(table.getPrice()<=0){
                workbook.close();
                throw new BadRequestException("Dòng "+index+": Giá thuê bàn phải lớn hơn 0");
            }
            tables.add(table);
        }
        workbook.close();
        return tables;
    }
    /*
    * update table
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public TableResponseDTO update(TableUpdateRequestDTO dto){
        TableE table = tableRepository.findById(dto.getId()).orElseThrow();
        table.copyProperties(dto);
        table = tableRepository.save(table);
        return new TableResponseDTO(table);
    }
    /*
    * add comment
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public boolean addComment(Long tableId, CommentTable comment){
        // if user commented
        try {
            if(commentTableRepository.findByTableIdAndUserId(tableId, comment.getUser().getId())!=null)
                return commentTableRepository.update(comment.getStar(), comment.getComment(), tableId, comment.getUser().getId())>0;
            return commentTableRepository.save(comment.getStar(), comment.getComment(), tableId, comment.getUser().getId(), comment.getUser().getUsername())>=1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /*
    *
    * */
    public List<PeriodsTimeBookedTableDTO> getFreeTimeOfTable(Date fromI, Date toI, Pageable pageable, String accessToken){
        List<PeriodsTimeBookedTableDTO> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String from = format.format(fromI);
        String to = format.format(toI);
        // lấy danh sách các bàn, trong đó có thời gian đặt của từng bàn
        String url = "http://localhost:9003/api/booking/booked_table/get_all_period_rent_table?from={from}&to={to}";
        HttpHeaders headers = new HttpHeaders();
        headers.set("access_token", accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers); 
        ResponseEntity<ResponseListTableFreeTimeDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                ResponseListTableFreeTimeDTO.class,
                from,to
        );
        List<Long> listNotIn = new ArrayList<>();
        listNotIn.add((long)-1);
        response.getBody().getData().stream().forEach((ptbt)->{
            listNotIn.add(ptbt.getId());
        });
        // lay danh sach cac ban trong
        final String url1 = "http://localhost:9003/api/booking/booked_table/get_period_rent_table/{id}?from={from}&to={to}";
        tableRepository.findAllNotIn(listNotIn,pageable).getContent().stream().forEach((t)->{
            PeriodsTimeBookedTableDTO periodsTimeBookedTableDTO = new PeriodsTimeBookedTableDTO();
            periodsTimeBookedTableDTO.setTimes(new ArrayList<>());
            BeanUtils.copyProperties(t, periodsTimeBookedTableDTO);
            long id = t.getId();
            ResponseEntity<ResponseDTO> resp1 = restTemplate.exchange(
                    url1,
                    HttpMethod.GET,
                    request,
                    ResponseDTO.class,
                    id,from,to
            );
            LinkedHashMap<String,List<Object>> map = (LinkedHashMap<String, List<Object>>) resp1.getBody().getData();
            map.get("times").stream().forEach((obj)->{
                LinkedHashMap<String,String> time = (LinkedHashMap<String, String>) obj;
                try {
                    PeriodTimeResponseDTO timeResp = new PeriodTimeResponseDTO(format.parse(time.get("from")), format.parse(time.get("to")));
                    periodsTimeBookedTableDTO.getTimes().add(timeResp);
                } catch (ParseException e) {
                }
            });
            list.add(periodsTimeBookedTableDTO);
        });
        return list;
    }
}