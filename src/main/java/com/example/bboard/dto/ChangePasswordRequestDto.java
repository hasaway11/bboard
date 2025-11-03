package com.example.bboard.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class ChangePasswordRequestDto {
  @NotEmpty
  @Pattern(regexp="^[A-Za-z0-9]{6,10}$")
  private String currentPassword;

  @NotEmpty
  @Pattern(regexp="^[A-Za-z0-9]{6,10}$")
  private String newPassword;
}
