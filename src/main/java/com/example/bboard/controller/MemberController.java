package com.example.bboard.controller;

import com.example.bboard.service.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberController {
  @Autowired
  private MemberService memberService;

  @GetMapping("/member/join")
  public void join() {}

  // Job-M01 : (REST. /api/member/check-username)
  // ResponseEntity는 실행결과 + http 상태코드
  @PreAuthorize("isAnonymous()")
  @GetMapping("/api/member/check-username")
  public ResponseEntity<String> checkUsernameAvailable(@RequestParam String username) {
    boolean avaialble = memberService.usernameAvailable(username);
    if(avaialble)
      return ResponseEntity.ok("아이디가 사용가능합니다");
    return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디가 사용중입니다");
  }
}
