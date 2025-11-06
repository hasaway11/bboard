package com.example.bboard.entity;

import lombok.*;

import java.time.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
  @Setter
  private long pno;
  private String title;
  private String content;
  private String writer;
  private LocalDateTime writeTime = LocalDateTime.now();
  private long readCnt = 0;
  private long goodCnt = 0;
  private long badCnt = 0;
}
