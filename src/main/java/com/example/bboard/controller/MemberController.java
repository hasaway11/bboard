package com.example.bboard.controller;

import com.example.bboard.dto.*;
import com.example.bboard.service.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberController {
  @Autowired
  private MemberService memberService;

  @PreAuthorize("isAnonymous()")
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

  // M-02
  @PreAuthorize("isAnonymous()")
  @PostMapping("/member/join")
  public String join(@ModelAttribute @Valid MemberJoinDto dto) {
    memberService.join(dto);
    return "redirect:/member/login";
  }

  // 찾기 화면 보여주기
  @PreAuthorize("isAnonymous()")
  @GetMapping("/member/find")
  public void find() {
  }

//  // M-03. 아이디 찾기
//  @GetMapping("/api/member/find-username")
//  public ResponseEntity<String> findUsername(@RequestParam String email) {
//    String username = memberService.findUsername(email);
//    if(username!=null)
//      return ResponseEntity.ok(username);
//    return ResponseEntity.status(409).body("사용자를 찾을 수 없습니다");
//  }
//
//  // M-04. 임시 비밀번호를 생성해 가입한 이메일로 보낸다
//  @PostMapping("/api/member/reset-password")
//  public ResponseEntity<String> resetPassword(@RequestParam String username) {
//    boolean result = memberService.resetPassword(username);
//    if(result==true)
//      return ResponseEntity.ok("임시비밀번호를 가입하신 이메일로 보냈습니다.");
//    return ResponseEntity.status(409).body("사용자를 찾을 수 없습니다");
//  }

}
