package com.dattp.productservice.service;

import com.dattp.productservice.config.kafka.KafkaTopicConfig;
import com.dattp.productservice.dto.kafka.bookedtable.BookedTableResponseDTO;
import com.dattp.productservice.dto.kafka.booking.BookingResponseDTO;
import com.dattp.productservice.entity.TableE;
import com.dattp.productservice.entity.state.TableState;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BookingService extends com.dattp.productservice.service.Service {
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public void checkBooking(BookingResponseDTO dto) {
    //check table
    if (Objects.isNull(dto.getBookedTables())) dto.setBookedTables(new ArrayList<>());
    List<Long> tableIds = dto.getBookedTables().stream()
        .map(BookedTableResponseDTO::getTableId).toList();
    Map<Long, TableE> tableMap = tableStorage.findAllTableReadyByIdInAndStateIn(tableIds).stream()
        .collect(Collectors.toMap(TableE::getId, Function.identity()));
    dto.getBookedTables().forEach(bt -> {
      TableE tableDB = tableMap.get(bt.getTableId());
      if (Objects.isNull(tableDB)) {
        bt.setState(TableState.DELETE);
      } else {
        bt.setState(tableDB.getState());
        bt.setName(tableDB.getName());
        bt.setPrice(tableDB.getPrice());
        bt.setImage(tableDB.getImage());
      }
    });
    //produce
    kafkaService.send(KafkaTopicConfig.PROCESS_BOOKING_TOPIC, dto);
  }
}
