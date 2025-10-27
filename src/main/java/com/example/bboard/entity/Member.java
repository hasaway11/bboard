package com.example.bboard.entity;

import lombok.*;

import java.time.*;

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
}
