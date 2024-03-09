package com.dattp.productservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO{
    private int code;
    private String message;
    private Object data;
    @Builder
    public ResponseDTO(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public ResponseDTO(){super();}
}