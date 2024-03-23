package com.dattp.productservice.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentTableResponseDTO {
    private Long id;
    private int star;
    private String comment;
    private Long userId;
    private String username;

    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date date;
    public CommentTableResponseDTO() {
    }
}