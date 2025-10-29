package com.example.bboard.entity;

import lombok.*;

import java.time.*;

@Getter
public class Post {
  private long pno;
  private String title;
  private String content;
  private String writer;
  private LocalDateTime write_time;
  private long readCnt;
  private long goodCnt;
  private long badCnt;
}
