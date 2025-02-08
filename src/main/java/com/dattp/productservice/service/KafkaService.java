package com.dattp.productservice.service;

import com.dattp.productservice.controller.user.response.DishDetailUserResponseResponse;
import com.dattp.productservice.kafkalisteners.dto.booking.BookingResponseDTO;
import com.dattp.productservice.controller.user.response.TableDetailUserResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaService {
  @Autowired
  @Lazy
  private KafkaTemplate<String, DishDetailUserResponseResponse> kafkaTemplateDish;
  @Autowired
  @Lazy
  private KafkaTemplate<String, TableDetailUserResponse> kafkaTemplateTable;
  @Autowired
  @Lazy
  private KafkaTemplate<String, BookingResponseDTO> kafkaTemplateBooking;

  @Async("taskExecutor")
  public void send(String topic, Object data) {
    try {
      if (data instanceof DishDetailUserResponseResponse)
        kafkaTemplateDish.send(topic, ((DishDetailUserResponseResponse) data).getId().toString(), (DishDetailUserResponseResponse) data);
      else if (data instanceof TableDetailUserResponse)
        kafkaTemplateTable.send(topic, ((TableDetailUserResponse) data).getId().toString(), (TableDetailUserResponse) data);
      else if (data instanceof BookingResponseDTO)
        kafkaTemplateBooking.send(topic, ((BookingResponseDTO) data).getId().toString(), (BookingResponseDTO) data);


      else log.warn("======> KafkaService::send::{}::{} NOT CONFIG SEND KAFKA!!!", topic, data);
      log.debug("======> KafkaService::send::{}::{} SUCCESS!!!", topic, data);
    } catch (Exception e) {
      log.error("======> KafkaService::send::{}::{}::Exception:{}", topic, data, e.getMessage());
    }
  }
}