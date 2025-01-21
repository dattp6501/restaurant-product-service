package com.dattp.productservice.service;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TelegramService extends com.dattp.productservice.service.Service {
  @Value("${telegram.enable_monitor_system}")
  private Boolean ENABLE_MONITOR_SYSTEM;

  @Value("${telegram.monitor_bot_token}")
  private String MONITOR_BOT_TOKEN;

  @Value("${telegram.monitor_bot_chat_id}")
  private String MONITOR_BOT_CHAT_ID;

  public void sendNotificatonMonitorSystem(String message) {
    if (ENABLE_MONITOR_SYSTEM) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      JsonObject request = new JsonObject();
      request.addProperty("text", "\uD83D\uDCD5 " + message);
      request.addProperty("parse_mode", "HTML");
      request.addProperty("disable_web_page_preview", false);
      request.addProperty("chat_id", MONITOR_BOT_CHAT_ID);

      HttpEntity<Object> requestEntity = new HttpEntity<>(request.toString(), headers);

      String url = String.format("https://api.telegram.org/bot%s/sendMessage", MONITOR_BOT_TOKEN);
      restTemplate.postForObject(url, requestEntity, Object.class);
    }
  }
}
