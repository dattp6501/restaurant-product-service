package com.dattp.productservice.service;

import com.dattp.productservice.config.kafka.KafkaTopicConfig;
import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.controller.manager.response.TableDetailManagerResponse;
import com.dattp.productservice.controller.user.dto.CommentTableRequestDTO;
import com.dattp.productservice.controller.user.dto.TableCreateRequestDTO;
import com.dattp.productservice.controller.user.dto.TableUpdateRequestDTO;
import com.dattp.productservice.controller.user.response.CommentTableResponseDTO;
import com.dattp.productservice.controller.user.response.TableDetailUserResponse;
import com.dattp.productservice.entity.CommentTable;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.entity.state.TableState;
import com.dattp.productservice.exception.BadRequestException;
import com.dattp.productservice.pojo.PeriodTime;
import com.dattp.productservice.base.response.table.TableOverviewResponse;
import com.dattp.productservice.response.PageSliceResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Log4j2
public class TableService extends com.dattp.productservice.service.Service {
  @Autowired
  @Lazy
  private TableService self;

  //================================================================================
  //=================================   USER   =====================================
  //================================================================================
  /*
   * get list table
   * */
  @Cacheable(value = RedisKeyConfig.PREFIX_TABLE, key = "@redisKeyConfig.genKeyPage(#pageable)")
  public PageSliceResponse<TableOverviewResponse> getTableOverview(Pageable pageable) {
    Slice<TableOverviewResponse> tableS = tableRepository.findAllByStateIn(List.of(TableState.ACTIVE), pageable)
        .map(TableOverviewResponse::new);
    //get free time
    tableS.forEach(t -> {
      String keyFree = RedisKeyConfig.genKeyTableFreeTime(t.getId());
      List<PeriodTime> tableFreeTime = redisService.getList(keyFree, PeriodTime.class);
      if (Objects.nonNull(tableFreeTime)) t.setFreeTime(tableFreeTime);
    });
    return PageSliceResponse.createFrom(
        tableS
    );
  }

  @Cacheable(value = RedisKeyConfig.PREFIX_TABLE, key = "@redisKeyConfig.genKeyNoType(#id)")
  public TableDetailUserResponse getDetailUser(Long id) {
    return new TableDetailUserResponse(
        tableRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(String.format("table(id=%d) not found", id)))
    );
  }

  /*
   * add comment
   * */
  @Caching(evict = {
      @CacheEvict(value = RedisKeyConfig.PREFIX_TABLE_COMMENT, allEntries = true),
  })
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public void addComment(CommentTableRequestDTO CTR) {
    CommentTable comment = new CommentTable(CTR);
    comment.setUser(new User(jwtService.getUserId(), jwtService.getUsername()));
    //save to db
    if (commentTableRepository.findByTableIdAndUserId(CTR.getTableId(), comment.getUser().getId()) != null)
      commentTableRepository.update(
          comment.getStar(), comment.getComment(), CTR.getTableId(), comment.getUser().getId(),
          comment.getDate()
      );
    else
      commentTableRepository.save(
          comment.getStar(), comment.getComment(), CTR.getTableId(), comment.getUser().getId(),
          comment.getUser().getUsername(), comment.getDate()
      );
  }

  @Cacheable(value = RedisKeyConfig.PREFIX_TABLE_COMMENT, key = "@redisKeyConfig.genKeyPage(#tableId,#pageable)")
  public List<CommentTableResponseDTO> getListCommentTable(Long tableId, Pageable pageable) {
    return commentTableRepository.findCommentTablesByTableId(tableId, pageable).toList()
        .stream().map(CommentTableResponseDTO::new)
        .collect(Collectors.toList());
  }


  //================================================================================
  //=================================   ADMIN   =====================================
  //================================================================================
  /*
   * get list table
   * */
  public PageSliceResponse<TableDetailManagerResponse> findListTableManager(Pageable pageable) {
    return PageSliceResponse.createFrom(
        tableRepository.findAllBy(pageable).map(TableDetailManagerResponse::new)
    );
  }

  /*
   * get table detail
   * */
  public TableDetailUserResponse getDetailManager(Long id) {
    return new TableDetailUserResponse(
        tableRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(String.format("table(id=%d) not found", id)))
    );
  }

  /*
   * create table
   * */
  @Caching(evict = {
      @CacheEvict(value = RedisKeyConfig.PREFIX_DISH, allEntries = true),
  })
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public TableDetailUserResponse create(TableCreateRequestDTO tableReq) {
    //save to db
    TableE table = tableRepository.save(new TableE(tableReq));
    //response
    TableDetailUserResponse resp = new TableDetailUserResponse(table);
    kafkaService.send(KafkaTopicConfig.NEW_TABLE_TOPIC, resp);
    return resp;
  }

  /*
   * update table
   * */
  @Caching(evict = {
      @CacheEvict(value = RedisKeyConfig.PREFIX_DISH, allEntries = true),
  })
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public TableDetailUserResponse update(TableUpdateRequestDTO dto) {
    TableE table = tableRepository.findById(dto.getId())
        .orElseThrow(() -> new BadRequestException(String.format("table(id=%d) not found", dto.getId())));
    table.copyProperties(dto);
    //save to db
    table = tableRepository.save(table);
    //response
    TableDetailUserResponse resp = new TableDetailUserResponse(table);
    kafkaService.send(KafkaTopicConfig.UPDATE_TABLE_TOPIC, resp);
    return resp;
  }

  /*
   * create table with excel
   * */
  public Boolean createByExcel(InputStream inputStream) throws IOException {
    List<TableE> tables = readXlsxTable(inputStream);
    tables = self.saveListTable(tables);
    //notification
    tables.forEach(t -> {
          //send kafka
          kafkaService.send(KafkaTopicConfig.NEW_TABLE_TOPIC, new TableDetailUserResponse(t));
        }
    );
    return true;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public List<TableE> saveListTable(List<TableE> list) {
    return tableRepository.saveAll(list);
  }

  public List<TableE> readXlsxTable(InputStream inputStream) throws IOException {
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
    while (it.hasNext()) {
      index++;
      boolean isRowEmpty = true;
      Row row = it.next();
      TableE table = new TableE();
      table.setState(TableState.ACTIVE);
      for (int i = 0; i < 6; i++) {
        if (i == COLUMN_INDEX_NAME) {
          if (row.getCell(i) != null && !row.getCell(i).getStringCellValue().isEmpty()) {
            table.setName(row.getCell(i).getStringCellValue());
            isRowEmpty = false;
          }
          continue;
        }
        if (i == COLUMN_INDEX_AMOUNT_OF_PEOPLE) {
          if (row.getCell(i) != null) {
            table.setAmountOfPeople((int) row.getCell(i).getNumericCellValue());
            isRowEmpty = false;
          }
          continue;
        }
        if (i == COLUMN_INDEX_PRICE) {
          if (row.getCell(i) != null) {
            table.setPrice((float) row.getCell(i).getNumericCellValue());
            isRowEmpty = false;
          }
          continue;
        }
        if (i == COLUMN_INDEX_FROM) {
          if (row.getCell(i) != null) {
            table.setFrom(row.getCell(i).getDateCellValue());
            isRowEmpty = false;
          }
          continue;
        }
        if (i == COLUMN_INDEX_TO) {
          if (row.getCell(i) != null) {
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
      if (isRowEmpty) continue;//if row empty
      // row not empty
      if (table.getName() == null || table.getName().isEmpty()) {
        workbook.close();
        throw new BadRequestException("Dòng " + index + ": Tên bàn không được để trống");
      }
      if (table.getAmountOfPeople() <= 0) {
        workbook.close();
        throw new BadRequestException("Dòng " + index + ": Số người ngồi phải lớn hơn 0");
      }
      if (table.getPrice() <= 0) {
        workbook.close();
        throw new BadRequestException("Dòng " + index + ": Giá thuê bàn phải lớn hơn 0");
      }
      tables.add(table);
    }
    workbook.close();
    return tables;
  }
}