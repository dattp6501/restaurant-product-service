package com.dattp.productservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


import com.dattp.productservice.config.redis.RedisKeyConfig;
import com.dattp.productservice.dto.dish.DishCreateRequestDTO;
import com.dattp.productservice.dto.dish.DishResponseDTO;
import com.dattp.productservice.dto.dish.DishUpdateRequestDTO;
import com.dattp.productservice.entity.state.DishState;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dattp.productservice.entity.CommentDish;
import com.dattp.productservice.entity.Dish;
import com.dattp.productservice.exception.BadRequestException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DishService extends com.dattp.productservice.service.Service {
    /*
     * get list dish
     * */
    public List<DishResponseDTO> getDishs(Pageable pageable){
      return dishStorage.findListFromCacheAndDB(pageable)
        .stream().map(DishResponseDTO::new)
        .collect(Collectors.toList());
    }
    /*
    * get detail dish
    * */
    public DishResponseDTO getDetail(Long id){
        return new DishResponseDTO(dishStorage.getDetailFromCacheAndDb(id));
    }
    /*
    * create dish
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public DishResponseDTO create(DishCreateRequestDTO dishReq){
        Dish dish = new Dish(dishReq);
        dish = dishRepository.save(dish);
        // cache
        redisService.putHash(dish.getId().toString(), RedisKeyConfig.genKeyDish(dish.getId()), dish, 0);

        return new DishResponseDTO(dish);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public DishResponseDTO update(DishUpdateRequestDTO dto){
        Dish dish = dishRepository.findById(dto.getId()).orElseThrow();
        dish.copyProperties(dto);
        dish = dishRepository.save(dish);
        // cache
        redisService.putHash(dish.getId().toString(), RedisKeyConfig.genKeyDish(dish.getId()), dish, 0);
        return new DishResponseDTO();
    }
    /*
    * create dish by excel
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Boolean createByExcel(InputStream inputStream) throws IOException {
        List<Dish> listDish = readXlsxDish(inputStream);
        listDish = dishRepository.saveAll(listDish);
        // cache
        listDish.forEach(dish -> redisService.putHash(dish.getId().toString(), RedisKeyConfig.genKeyDish(dish.getId()), dish, 0));
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
    /*
    * add comment to dish
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public boolean addComment(Long dishId, CommentDish comment){
        if(CommentDishRepository.findByDishIdAndUserId(dishId, comment.getUser().getId())!=null)
            return CommentDishRepository.update(comment.getStar(), comment.getComment(), dishId, comment.getUser().getId(), comment.getDate())>0;
        return CommentDishRepository.save(comment.getStar(), comment.getComment(), dishId, comment.getUser().getId(), comment.getUser().getUsername(), comment.getDate())>=1;
    }
}
