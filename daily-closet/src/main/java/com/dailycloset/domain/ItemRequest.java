package com.dailycloset.domain;

import lombok.*;

/**
 * 베스트 아이템 조회하기 위한 Request 객체
 */
@Getter @Setter
@AllArgsConstructor
public class ItemRequest {
  @Builder.Default
  private String categoryName = "best";
  @Builder.Default
  private int limit = 20;
  @Builder.Default
  private String sort = "name";
  @Builder.Default
  private int page = 0;

}
