package com.example.bboard.entity;

import lombok.*;

@Getter
@AllArgsConstructor
public enum Level {
  BRONZE("소중한 분"), SILVER("귀하신 분"), GOLD("천생 연분");

  private String kor;
}

// Level level = Level.BRONZE;
// 1. 저장할 때 "BRONZE"라고 저장
// 2. 출력할 때 level.name() -> BRONZE
// 3. 필드를 추가하면 level.name() -> "소중한 분"