package com.example.bboard.dto;

import lombok.*;

@Data
public class FindAllDto {
  private long pno;
  private String title;
  private String writer;
  private String writeTime;
  private long readCnt;
  private long cnt;
}