package com.dattp.productservice.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaService {
//  @Autowired @Lazy private KafkaTemplate<String, DishResponseDTO> kafkaTemplateDish;
//  @Autowired @Lazy private KafkaTemplate<String, TableResponseDTO> kafkaTemplateTable;
//  @Autowired @Lazy private KafkaTemplate<String, BookingResponseDTO> kafkaTemplateBooking;

  @Async("taskExecutor")
  public void send(String topic, Object data){
    try {
//      if(data instanceof DishResponseDTO) kafkaTemplateDish.send(topic, ((DishResponseDTO)data).getId().toString(), (DishResponseDTO) data);
//      else if(data instanceof TableResponseDTO) kafkaTemplateTable.send(topic, ((TableResponseDTO) data).getId().toString(), (TableResponseDTO) data);
//      else if(data instanceof BookingResponseDTO) kafkaTemplateBooking.send(topic, ((BookingResponseDTO) data).getId().toString(), (BookingResponseDTO) data);


//      else log.warn("======> KafkaService::send::{}::{} NOT CONFIG SEND KAFKA!!!", topic, data);
      log.debug("======> KafkaService::send::{}::{} SUCCESS!!!", topic, data);
    }catch (Exception e){
      log.error("======> KafkaService::send::{}::{}::Exception:{}",topic, data, e.getMessage());
    }
  }
}