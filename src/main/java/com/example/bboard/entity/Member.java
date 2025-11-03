package com.example.bboard.entity;

import com.example.bboard.dto.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

@Getter
@NoArgsConstructor
public class Member {
  private String username;
  private String password;
  private String email;
  private String profile;
  private LocalDate joinDay = LocalDate.now();
  private Role role = Role.USER;
  private Level levels = Level.BRONZE;
  private long failedAttempts = 0;
  private boolean isLock = false;

  public Member(String username, String password, String email, String profile) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.profile = profile;
  }

  public MemberResponseDto toResponseDto() {
    String profileUrl = "/api/profile-image/profile?profile=" + profile;

    // 오라클에서 문자열로 변환 to_char(join_day, "서식")
    // 오라클 패턴에서 월은 mm, 분은 mi

    // 자바에서 LocalDate로 받은 다음 문자열로 변환하려면
    // 자바 패턴에서 월은 MM, 분은 mm
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    String joinDayString = dtf.format(this.joinDay);

    long days = ChronoUnit.DAYS.between(this.joinDay, LocalDate.now());

    return new MemberResponseDto(email, profileUrl, profile, joinDayString, levels.getKor(), days);
  }
}
