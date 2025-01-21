package com.dattp.productservice.kafkalisteners;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class KafkaBookingListener extends com.dattp.productservice.kafkalisteners.KafkaListener {
//    @KafkaListener(topics = KafkaTopicConfig.NEW_BOOKING_TOPIC, groupId="com.dattp.restaurant.product.new_order", containerFactory = "factoryBooking")
//    public void listenerNewBookingTopic(@Payload BookingResponseDTO dto, Acknowledgment acknowledgment){
//        try {
//            bookingService.checkBooking(dto);
//            acknowledgment.acknowledge();
//        }catch (Exception e){
//            log.error("=====================>  listenerNewBookingTopic:Exception:{}", e.getMessage());
//        }
//
//    }
}