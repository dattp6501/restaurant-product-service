package com.dattp.productservice.dto.kafka.booking;

public enum BookingState {
  NEW,
  PROCESSING,
  SUCCESS,
  CANCEL,
  DELETE
}