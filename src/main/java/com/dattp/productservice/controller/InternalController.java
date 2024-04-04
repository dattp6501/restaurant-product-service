package com.dattp.productservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class InternalController {
  @GetMapping("/isRunning")
  public ResponseEntity<Boolean> isRunning(){
    return ResponseEntity.ok(true);
  }
}