package com.example.bboard.dto;

import lombok.*;

// 내부 클래스를 이용해서 DTO를 정리
// PostDto는 객체를 만들지 않고 단순한 컨테이너로 사용

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class PostDto {
  @Data
  public static class ListResponse {
    private long pno;
    private String title;
    private String writer;
    private String writeTime;
    private long readCnt;
  }
}
