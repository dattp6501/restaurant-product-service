package com.dattp.productservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


import com.dattp.productservice.config.kafka.KafkaTopicConfig;
import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.dto.dish.CommentDishResponseDTO;
import com.dattp.productservice.dto.dish.DishCreateRequestDTO;
import com.dattp.productservice.dto.dish.DishResponseDTO;
import com.dattp.productservice.dto.dish.DishUpdateRequestDTO;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.pojo.DishOverview;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.exception.BadRequestException;

@Service
@Log4j2
public class DishService extends com.dattp.productservice.service.Service {
    //===============================================================================
    //==============================    USER   ===================================
    //=============================================================================
    /*
     * get list dish
     * */
    public List<DishOverview> getDishsOverview(Pageable pageable){
        return dishStorage.findListFromCacheAndDB(pageable);
    }

    public List<DishOverview> getDishsHot(Pageable pageable){List<DishOverview> dishs = dishStorage.findListFromCacheAndDB(pageable);
        if(dishs.size()>10) return dishs.subList(0, 10);
        return dishs;
    }
    /*
     * get detail dish
     * */
    public DishResponseDTO getDetailFromCache(Long id){
        return new DishResponseDTO(dishStorage.getDetailFromCacheAndDb(id));
    }

    /*
     * add comment to dish
     * */
    public boolean addComment(CommentDish comment){
        comment.setUser(new User(jwtService.getUserId(), jwtService.getUsername()));
        //save to db
        dishStorage.addCommentDish(comment);
        //cache
        String key = RedisKeyConfig.genKeyCommentDish(comment.getDish().getId());
        if(!redisService.hasKey(key))
            dishStorage.initCommentDishCache(comment.getDish().getId());
        else redisService.addElemntHash(key, comment.getUser().getId().toString(), comment, RedisService.CacheTime.ONE_WEEK);

        return true;
    }

    /*
    * get list comment
    * */
    public List<CommentDishResponseDTO> getListComment(Long dishId, Pageable pageable){
        return dishStorage.getListCommentFromCacheAndDB(dishId, pageable)
          .stream().map(CommentDishResponseDTO::new)
          .collect(Collectors.toList());
    }



    //===============================================================================
    //==============================    ADMIN   ===================================
    //=============================================================================
    public List<DishResponseDTO> getDishsFromDB(Pageable pageable){
        return dishStorage.findAll(pageable).stream()
          .map(DishResponseDTO::new)
          .collect(Collectors.toList());
    }

    public DishResponseDTO getDetailFromDB(Long id){
        return new DishResponseDTO(dishStorage.getDetailDishFromDB(id));
    }

    /*
    * create dish
    * */
    public DishResponseDTO create(DishCreateRequestDTO dishReq){
        //save db
        Dish dish = dishStorage.saveToDB(new Dish(dishReq));
        //response
        DishResponseDTO resp = new DishResponseDTO(dish);
        kafkaService.send(KafkaTopicConfig.NEW_DISH_TOPIC, resp);
        return resp;
    }

    public DishResponseDTO update(DishUpdateRequestDTO dto){
        Dish dish = dishStorage.getDetailDishFromDB(dto.getId());
        dish.copyProperties(dto);
        //save db
        dish = dishStorage.saveToDB(dish);
        //resp
        DishResponseDTO resp = new DishResponseDTO(dish);
        kafkaService.send(KafkaTopicConfig.UPDATE_DISH_TOPIC, resp);
        return resp;
    }
    /*
    * create dish by excel
    * */
    public Boolean createByExcel(InputStream inputStream) throws IOException {
        List<Dish> listDish = readXlsxDish(inputStream);
        listDish = dishStorage.saveAll(listDish);
        listDish.forEach(dish -> {
            //send kafka
            kafkaService.send(KafkaTopicConfig.NEW_TABLE_TOPIC, new DishResponseDTO(dish));
        });
        return true;
    }
    public List<Dish> readXlsxDish(InputStream inputStream) throws IOException{
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
        while(it.hasNext()) {
            Dish dish = new Dish();
            dish.setState(DishState.ACTIVE);
            Row row = it.next();
            for(int i=0; i<3; i++){
                if(i==COLUMN_INDEX_NAME){
                    if(row.getCell(i)==null || row.getCell(i).getStringCellValue().isEmpty()){
                        workbook.close();
                        throw new BadRequestException("Dòng "+index+": Tên món ăn không được để trống");
                    }
                    dish.setName(row.getCell(i).getStringCellValue());
                    continue;
                }
                if(i==COLUMN_INDEX_PRICE){
                    if(row.getCell(i)==null){
                        throw new BadRequestException("Dòng "+index+": Giá món ăn không được để trống");
                    }
                    if(row.getCell(i).getNumericCellValue()<=0){
                        workbook.close();
                        throw new BadRequestException("Dòng "+index+": Giá món ăn phải lớn hơn 0");
                    }
                    dish.setPrice((float)row.getCell(i).getNumericCellValue());
                    continue;
                }
                if(i==COLUMN_INDEX_DESCRIPTION){
                    if(row.getCell(i)!=null){
                        dish.setDescription(row.getCell(i).getStringCellValue());
                        workbook.close();
                    }
                }
                if(i==COLUMN_INDEX_IMAGE){
                    if(row.getCell(i)!=null){
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
