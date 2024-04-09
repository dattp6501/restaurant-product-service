package com.dattp.productservice.dto.kafka.bookedtable;

import com.dattp.productservice.entity.state.TableState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class BookedTableResponseDTO {
    private Long id;
    private Long tableId;
    private String name;
    private String image;
    private TableState state;
    private Float price;

    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private LocalDateTime from;

    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private LocalDateTime to;

    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private LocalDateTime createAt;

    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private LocalDateTime updateAt;
    public BookedTableResponseDTO() {super();}
    @Override
    public boolean equals(Object obj) {
        BookedTableResponseDTO other = (BookedTableResponseDTO) obj;
        return Objects.equals(this.id, other.id);
    }
}