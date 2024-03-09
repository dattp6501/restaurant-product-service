package com.dattp.productservice.dto.resttemplate;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseListTableFreeTimeDTO {
    private int code;
    private String message;
    private List<PeriodsTimeBookedTableDTO> data;
    public ResponseListTableFreeTimeDTO(){super();}
}
