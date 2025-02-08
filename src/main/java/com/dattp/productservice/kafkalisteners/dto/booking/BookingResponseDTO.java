package com.dattp.productservice.kafkalisteners.dto.booking;

import com.dattp.productservice.kafkalisteners.dto.bookeddish.BookedDishResponseDTO;
import com.dattp.productservice.kafkalisteners.dto.bookedtable.BookedTableResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookingResponseDTO {
  private Long id;

  private Long CustomerId;

  private String custemerFullname;

  private int state;

  private boolean paid;

  @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
  private LocalDateTime from;

  @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
  private LocalDateTime to;

  private float deposits;

  private String description;

  @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
  private LocalDateTime createAt;

  @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
  private LocalDateTime updateAt;

  private List<BookedTableResponseDTO> bookedTables;

  private List<BookedDishResponseDTO> dishs;

  public BookingResponseDTO() {
    super();
  }
}