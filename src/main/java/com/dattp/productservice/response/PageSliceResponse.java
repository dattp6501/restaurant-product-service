package com.dattp.productservice.response;

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
    return (PageSliceResponse<T>) new PageSliceResponse(pageData.getContent(), PageSliceResponse.Metadata.createFrom(pageData));
  }

  private List<T> data;
  private Metadata metadata;

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
