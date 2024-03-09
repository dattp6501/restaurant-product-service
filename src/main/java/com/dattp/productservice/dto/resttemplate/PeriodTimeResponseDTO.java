package com.dattp.productservice.dto.resttemplate;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class PeriodTimeResponseDTO {
    @JsonFormat(pattern="HH:mm:ss dd/MM/yyyy")
    private Date from;

    @JsonFormat(pattern="HH:mm:ss dd/MM/yyyy")
    private Date to;
    public PeriodTimeResponseDTO() {
    }
    public PeriodTimeResponseDTO(Date from, Date to) {
        this.from = from;
        this.to = to;
    }
}
