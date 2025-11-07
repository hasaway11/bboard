package com.example.bboard.entity;

import lombok.*;

import java.time.*;

@Data
public class Comment {
  private long cno;
  private String content;
  private String writer;
  private String writeTime;
  private long pno;
}
