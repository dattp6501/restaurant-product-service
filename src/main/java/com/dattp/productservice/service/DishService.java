package com.dattp.productservice.service;

import com.dattp.productservice.config.kafka.KafkaTopicConfig;
import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.controller.manager.response.DishDetailManageResponse;
import com.dattp.productservice.controller.user.dto.CommentDishRequestDTO;
import com.dattp.productservice.controller.user.dto.DishCreateRequestDTO;
import com.dattp.productservice.controller.user.dto.DishUpdateRequestDTO;
import com.dattp.productservice.controller.user.response.CommentDishResponse;
import com.dattp.productservice.controller.user.response.DishDetailUserResponseResponse;
import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.exception.BadRequestException;
import com.dattp.productservice.base.response.dish.DishOverviewResponse;
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
import java.util.stream.Collectors;

@Service
@Log4j2
public class DishService extends com.dattp.productservice.service.Service {
  @Autowired
  @Lazy
  private DishService self;

  //===============================================================================
  //==============================    USER   ===================================
  //=============================================================================
  /*
   * get list dish
   * */
  @Cacheable(value = RedisKeyConfig.PREFIX_DISH, key = "@redisKeyConfig.genKeyPage(#pageable)")
  public PageSliceResponse<DishOverviewResponse> findPageDish(Pageable pageable) {
    Slice<Dish> dishs = dishRepository.findDishesByStateIn(List.of(DishState.ACTIVE), pageable);
    return PageSliceResponse.createFrom(dishs.map(DishOverviewResponse::new));
  }

  @Cacheable(value = RedisKeyConfig.PREFIX_DISH, key = "@redisKeyConfig.genKeyNoType('hot')")
  public List<DishOverviewResponse> getListDishHot(Pageable pageable) {
    Slice<Dish> dishs = dishRepository.findDishesByStateIn(List.of(DishState.ACTIVE), pageable);
    List<DishOverviewResponse> response = dishs.stream().map(DishOverviewResponse::new).toList();
    if (response.size() > 10) return response.subList(0, 10);
    return response;
  }

  /*
   * get detail dish
   * */
  @Cacheable(value = RedisKeyConfig.PREFIX_DISH, key = "@redisKeyConfig.genKeyNoType(#id)")
  public DishDetailUserResponseResponse getDetail(Long id) {
    return new DishDetailUserResponseResponse(
        dishRepository.findById(id).orElseThrow(() ->
            new BadRequestException(String.format("dish(id=%d) not found", id)))
    );
  }

  /*
   * add comment to dish
   * */
  @Caching(evict = {
      @CacheEvict(value = RedisKeyConfig.PREFIX_DISH_COMMENT, allEntries = true),
  })
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public Long addComment(CommentDishRequestDTO CDR) {
    CommentDish comment = new CommentDish(CDR);
    comment.setUser(new User(jwtService.getUserId(), jwtService.getUsername()));
    //save to db
    Long dishId = comment.getDish().getId();
    if (commentDishRepository.findByDishIdAndUserId(dishId, comment.getUser().getId()) != null)
      commentDishRepository.update(comment.getStar(), comment.getComment(), dishId, comment.getUser().getId(), comment.getDate());
    else
      commentDishRepository.save(comment.getStar(), comment.getComment(), dishId, comment.getUser().getId(), comment.getUser().getUsername(), comment.getDate());

    return comment.getId();
  }

  /*
   * get list comment
   * */
  @Cacheable(value = RedisKeyConfig.PREFIX_DISH_COMMENT, key = "@redisKeyConfig.genKeyPage(#dishId,#pageable)")
  public List<CommentDishResponse> getListComment(Long dishId, Pageable pageable) {
    return commentDishRepository.findCommentDishesByDish_Id(dishId, pageable)
        .stream().map(CommentDishResponse::new)
        .collect(Collectors.toList());
  }


  //===============================================================================
  //==============================    MANAGER   ===================================
  //=============================================================================
  public List<DishDetailManageResponse> getListDishManager(Pageable pageable) {
    return dishRepository.findAll(pageable).stream()
        .map(DishDetailManageResponse::new)
        .collect(Collectors.toList());
  }

  public DishDetailUserResponseResponse getDetailFromDB(Long id) {
    return new DishDetailUserResponseResponse(dishStorage.getDetailDishFromDB(id));
  }

  /*
   * create dish
   * */
  @Caching(evict = {
      @CacheEvict(value = RedisKeyConfig.PREFIX_DISH, allEntries = true),
  })
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public DishDetailUserResponseResponse create(DishCreateRequestDTO dishReq) {
    //save db
    Dish dish = dishRepository.save(new Dish(dishReq));
    //response
    DishDetailUserResponseResponse resp = new DishDetailUserResponseResponse(dish);
    kafkaService.send(KafkaTopicConfig.NEW_DISH_TOPIC, resp);
    return resp;
  }

  @Caching(evict = {
      @CacheEvict(value = RedisKeyConfig.PREFIX_DISH, allEntries = true),
  })
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public DishDetailUserResponseResponse update(DishUpdateRequestDTO dto) {
    Dish dish = dishRepository.findById(dto.getId())
        .orElseThrow(() -> new BadRequestException(String.format("dish(id=%d) not found", dto.getId())));
    dish.copyProperties(dto);
    //save db
    dish = dishRepository.save(dish);
    //resp
    DishDetailUserResponseResponse resp = new DishDetailUserResponseResponse(dish);
    kafkaService.send(KafkaTopicConfig.UPDATE_DISH_TOPIC, resp);
    return resp;
  }

  /*
   * create dish by excel
   * */
  @Caching(evict = {
      @CacheEvict(value = RedisKeyConfig.PREFIX_DISH, allEntries = true),
  })
  public Boolean createByExcel(InputStream inputStream) throws IOException {
    List<Dish> listDish = readXlsxDish(inputStream);
    listDish = self.saveListDish(listDish);
    listDish.forEach(dish -> {
      //send kafka
      kafkaService.send(KafkaTopicConfig.NEW_TABLE_TOPIC, new DishDetailUserResponseResponse(dish));
    });
    return true;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public List<Dish> saveListDish(List<Dish> listDish) {
    return dishRepository.saveAll(listDish);
  }

  public List<Dish> readXlsxDish(InputStream inputStream) throws IOException {
    final int COLUMN_INDEX_NAME = 0;
    final int COLUMN_INDEX_PRICE = 1;
    final int COLUMN_INDEX_DESCRIPTION = 2;
    final int COLUMN_INDEX_IMAGE = 3;
    List<Dish> dishs = new ArrayList<>();
    Workbook workbook = new XSSFWorkbook(inputStream);
    Sheet sheet = workbook.getSheetAt(0);
    Iterator<Row> it = sheet.iterator();
    it.next();
    int index = 1;
    while (it.hasNext()) {
      Dish dish = new Dish();
      dish.setState(DishState.ACTIVE);
      Row row = it.next();
      for (int i = 0; i < 3; i++) {
        if (i == COLUMN_INDEX_NAME) {
          if (row.getCell(i) == null || row.getCell(i).getStringCellValue().isEmpty()) {
            workbook.close();
            throw new BadRequestException("Dòng " + index + ": Tên món ăn không được để trống");
          }
          dish.setName(row.getCell(i).getStringCellValue());
          continue;
        }
        if (i == COLUMN_INDEX_PRICE) {
          if (row.getCell(i) == null) {
            throw new BadRequestException("Dòng " + index + ": Giá món ăn không được để trống");
          }
          if (row.getCell(i).getNumericCellValue() <= 0) {
            workbook.close();
            throw new BadRequestException("Dòng " + index + ": Giá món ăn phải lớn hơn 0");
          }
          dish.setPrice((float) row.getCell(i).getNumericCellValue());
          continue;
        }
        if (i == COLUMN_INDEX_DESCRIPTION) {
          if (row.getCell(i) != null) {
            dish.setDescription(row.getCell(i).getStringCellValue());
            workbook.close();
          }
        }
        if (i == COLUMN_INDEX_IMAGE) {
          if (row.getCell(i) != null) {
            // PictureData pictureData = (PictureData)row.getCell(i);

          }
        }
      }
      dishs.add(dish);
      index++;
    }
    workbook.close();
    return dishs;
  }
}
