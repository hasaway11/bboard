package com.example.bboard.dto;

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
}
