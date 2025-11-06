package com.example.bboard.dto;

import com.example.bboard.entity.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;
import java.util.*;

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

  @Data
  public static class WriteRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    public Post toEntity(String username) {
      return Post.builder().writer(username).title(title).content(content).build();
    }
  }

  @Data
  public static class PostResponse {
    private long pno;
    private String title;
    private String content;
    private String writer;
    private String writeTime;
    private long readCnt;
    private long goodCnt;
    private long badCnt;
    private List<Comment> comments;

    public void increaseReadCnt() {
      this.readCnt++;
    }
  }
}
