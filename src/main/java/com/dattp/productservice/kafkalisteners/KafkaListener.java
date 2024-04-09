package com.dattp.productservice.kafkalisteners;

import com.dattp.productservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class KafkaListener {
    @Autowired @Lazy BookingService bookingService;
}
