package com.dattp.productservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.dto.dish.DishCreateRequestDTO;
import com.dattp.productservice.dto.dish.DishResponseDTO;
import com.dattp.productservice.dto.dish.DishUpdateRequestDTO;
import com.dattp.productservice.entity.User;
import com.dattp.productservice.entity.state.DishState;
import com.dattp.productservice.pojo.DishOverview;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.exception.BadRequestException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DishService extends com.dattp.productservice.service.Service {
    //===============================================================================
    //==============================    USER   ===================================
    //=============================================================================
    /*
     * get list dish
     * */
    public List<DishResponseDTO> getDishsOverview(Pageable pageable){
        return dishStorage.findListFromCacheAndDB(pageable)
          .stream().map(DishResponseDTO::new)
          .collect(Collectors.toList());
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public boolean addComment(CommentDish comment){
        comment.setUser(new User(jwtService.getUserId(), jwtService.getUsername()));
        return dishStorage.addCommentDish(comment);
    }




    //===============================================================================
    //==============================    ADMIN   ===================================
    //=============================================================================
    public List<DishResponseDTO> getDishsFromDB(Pageable pageable){
        return dishRepository.findAll(pageable).stream()
          .map(DishResponseDTO::new)
          .collect(Collectors.toList());
    }

    public DishResponseDTO getDetailFromDB(Long id){
        return new DishResponseDTO(dishRepository.findById(id).orElseThrow());
    }

    /*
    * create dish
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public DishResponseDTO create(DishCreateRequestDTO dishReq){
        Dish dish = new Dish(dishReq);
        //response
        return new DishResponseDTO(dishStorage.addToCacheAndDb(dish));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public DishResponseDTO update(DishUpdateRequestDTO dto){
        Dish dish = dishRepository.findById(dto.getId()).orElseThrow();
        dish.copyProperties(dto);
        return new DishResponseDTO(dishStorage.updateFromCacheAndDb(dish));
    }
    /*
    * create dish by excel
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Boolean createByExcel(InputStream inputStream) throws IOException {
        List<Dish> listDish = readXlsxDish(inputStream);
        listDish = dishRepository.saveAll(listDish);
        // cache
        //list dish overview
        if(!redisService.hasKey(RedisKeyConfig.genKeyAllDishOverview())) dishStorage.initDishOverviewCache();
        listDish.forEach(dish -> {
            //list dish overview
            dishStorage.addOverviewDishToCache(dish);
            //detail dish
            dishStorage.addToCache(dish);
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
                    if(row.getCell(i)==null || row.getCell(i).getStringCellValue().equals("")){
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
