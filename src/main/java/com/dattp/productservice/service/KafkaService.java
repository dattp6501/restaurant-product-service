package com.dattp.productservice.service;

import com.dattp.productservice.dto.dish.DishResponseDTO;
import com.dattp.productservice.dto.table.TableResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaService {
  @Autowired @Lazy private KafkaTemplate<String, DishResponseDTO> kafkaTemplateDish;
  @Autowired @Lazy private KafkaTemplate<String, TableResponseDTO> kafkaTemplateTable;

  @Async("taskExecutor")
  public void send(String topic, Object data){
    try {
      if(data instanceof DishResponseDTO) kafkaTemplateDish.send(topic, (DishResponseDTO) data);
      else if(data instanceof TableResponseDTO) kafkaTemplateTable.send(topic, (TableResponseDTO) data);



      else log.debug("======> KafkaService::send::{}::{} NOT CONFIG SEND KAFKA!!!", topic, data);
      log.debug("======> KafkaService::send::{}::{} SUCCESS!!!", topic, data);
    }catch (Exception e){
      log.debug("======> KafkaService::send::{}::{}::Exception:{}",topic, data, e.getMessage());
    }
  }
}