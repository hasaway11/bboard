package com.example.bboard.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class MemberResponseDto {
  private String email;
  private String profileUrl;
  private String profileName;
  private String joinDay;
  private String level;
  private long days;
}
