package com.dattp.productservice.response;

import com.dattp.productservice.base.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageSliceResponse<T> {
  public static <T> PageSliceResponse<T> createFrom(Slice<T> pageData) {
    return (PageSliceResponse<T>) new PageSliceResponse(
        pageData.getContent(), PageSliceResponse.Metadata.createFrom(pageData),
        ErrorMessage.SUCCESS.getStatus().value(),
        ErrorMessage.SUCCESS.getMessage()
    );
  }

  private List<T> data;
  private Metadata metadata;
  private int code = ErrorMessage.SUCCESS.getStatus().value();
  private String message = ErrorMessage.SUCCESS.getMessage();

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Metadata {
    private boolean hasNext;

    public static Metadata createFrom(Slice slice) {
      return new Metadata(slice.hasNext());
    }
  }
}
