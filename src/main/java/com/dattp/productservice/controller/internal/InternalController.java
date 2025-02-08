package com.dattp.productservice.controller.internal;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class InternalController {
  @GetMapping(value = "/isRunning", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Boolean> isRunning() {
    return ResponseEntity.ok(true);
  }
}