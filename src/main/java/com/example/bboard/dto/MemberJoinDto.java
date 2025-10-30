package com.example.bboard.dto;

import com.example.bboard.entity.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.*;

@Data
public class MemberJoinDto {
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;
  @NotEmpty
  private String email;
  private String profile;

  public void init(String 암호화된_비밀번호, String 저장_프로필_이름) {
    this.password = 암호화된_비밀번호;
    this.profile = 저장_프로필_이름;
  }
}
