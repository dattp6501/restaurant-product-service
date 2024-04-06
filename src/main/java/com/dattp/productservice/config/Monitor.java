package com.dattp.productservice.config;


import com.dattp.productservice.service.TelegramService;
import com.dattp.productservice.utils.DateUtils;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;

@Configuration
@EnableScheduling
public class Monitor {
  @Autowired @Lazy private TelegramService telegramService;
  @Scheduled(initialDelay = 2000, fixedDelay = 1200000)
  public void isRunning(){
      String message =
        DateUtils.getcurrentLocalDateTime()
          .plusHours(6)
          .format(DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd"))
        +": PRODUCT ===> RUNNING";
      telegramService.sendNotificatonMonitorSystem(message);
  }
}