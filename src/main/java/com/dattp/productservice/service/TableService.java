package com.dattp.productservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.dto.table.CommentTableResponseDTO;
import com.dattp.productservice.dto.table.TableCreateRequestDTO;
import com.dattp.productservice.dto.table.TableResponseDTO;
import com.dattp.productservice.dto.table.TableUpdateRequestDTO;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.entity.state.TableState;
import com.dattp.productservice.pojo.TableOverview;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
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
    //================================================================================
    //=================================   USER   =====================================
    //================================================================================
    /*
     * get list table
     * */
    public List<TableOverview> getTableOverview(Pageable pageable){
        return tableStorage.findListFromCacheAndDB(pageable);
    }

    public TableResponseDTO getDetailFromCache(Long id){
        return new TableResponseDTO(tableStorage.getDetailFromCacheAndDB(id));
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
        Objects.requireNonNull(response.getBody()).getData().forEach((ptbt)->{
            listNotIn.add(ptbt.getId());
        });
        // lay danh sach cac ban trong
        final String url1 = "http://localhost:9003/api/booking/booked_table/get_period_rent_table/{id}?from={from}&to={to}";
        tableRepository.findAllNotIn(listNotIn,pageable).getContent().forEach((t)->{
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
            map.get("times").forEach((obj)->{
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
    /*
     * add comment
     * */
    public boolean addComment(CommentTable comment){
        comment.setUser(new User(jwtService.getUserId(), jwtService.getUsername()));
        //save to db
        tableStorage.addCommentTable(comment);
        //cache
        String key = RedisKeyConfig.genKeyCommentTable(comment.getTable().getId());
        if(!redisService.hasKey(key)) tableStorage.initCommentTableCache(comment.getTable().getId());
        redisService.addElemntHash(key, comment.getUser().getId().toString(), comment, RedisService.CacheTime.ONE_WEEK);
        return true;
    }

    public List<CommentTableResponseDTO> getListCommentTable(Long tableId, Pageable pageable){
        return tableStorage.getListCommentTableFromCacheAndDB(tableId, pageable)
          .stream().map(CommentTableResponseDTO::new)
          .collect(Collectors.toList());
    }


    //================================================================================
    //=================================   ADMIN   =====================================
    //================================================================================
    /*
    * get list table
    * */
    public List<TableResponseDTO> getAllFromDB(Pageable pageable){
        return tableRepository.findAll(pageable)
          .stream().map(TableResponseDTO::new)
          .collect(Collectors.toList());
    }
    /*
    * get table detail
    * */
    public TableResponseDTO getDetailDB(Long id){
        return new TableResponseDTO(tableStorage.getDetailFromDB(id));
    }
    /*
    * create table
    * */
    public TableResponseDTO create(TableCreateRequestDTO tableReq) {
        //save to db
        TableE table = tableStorage.saveToDB(new TableE(tableReq));
        //cache
        tableStorage.addToCache(table);
        tableStorage.addTableOverview(table);
        //response
        return new TableResponseDTO(table);
    }
    /*
     * update table
     * */
    public TableResponseDTO update(TableUpdateRequestDTO dto){
        TableE table = tableRepository.findById(dto.getId()).orElseThrow(()-> new BadRequestException(String.format("table(id=%d) not found", dto.getId())));
        table.copyProperties(dto);
        //save to db
        table = tableStorage.saveToDB(table);
        //cache
        tableStorage.addToCache(table);
        tableStorage.updateTableOverview(table);
        //response
        return new TableResponseDTO(table);
    }
    /*
    * create table with excel
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Boolean createByExcel(InputStream inputStream) throws IOException {
        List<TableE> tables = readXlsxTable(inputStream);
        tables = tableRepository.saveAll(tables);
        //cache
        //overview
        if(!redisService.hasKey(RedisKeyConfig.genKeyAllTableOverview()))
            tableStorage.initTableOverview();
        tables.forEach(t->{
            //detail
            tableStorage.addToCache(t);
            //over view
            tableStorage.addTableOverview(t);
          }
        );
        return true;
    }
    public List<TableE> readXlsxTable(InputStream inputStream) throws IOException{
        List<TableE> tables = new ArrayList<>();
        final int COLUMN_INDEX_NAME = 0;
        final int COLUMN_INDEX_AMOUNT_OF_PEOPLE = 1;
        final int COLUMN_INDEX_PRICE = 2;
        final int COLUMN_INDEX_FROM = 3;
        final int COLUMN_INDEX_TO = 4;
        //final int COLUMN_INDEX_DESCRIPTION = 5;
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
                    if(row.getCell(i)!=null && !row.getCell(i).getStringCellValue().isEmpty()) {
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
                if (row.getCell(i) != null) {
                table.setDescription(row.getCell(i).getStringCellValue());
                isRowEmpty = false;
                }
            }
            if(isRowEmpty) continue;//if row empty
            // row not empty
            if(table.getName()==null || table.getName().isEmpty()){
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
}