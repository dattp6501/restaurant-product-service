package com.dattp.productservice.dto.kafka;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingRequestKafkaDTO {
    private int state;
    private long id;
    private long CustomerId;

    private String custemerFullname;
    
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date date;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date from;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date to;
    private String description;
    private Collection<BookedTableRequestKafkaDTO> bookedTables;
    private List<BookedDishRequestKafkaDTO> dishs;
    public BookingRequestKafkaDTO() {
    }
}