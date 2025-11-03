package com.example.bboard.controller;

import com.example.bboard.dto.*;
import com.example.bboard.service.*;
import jakarta.servlet.http.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;

import java.security.*;
import java.util.*;

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

  // M-03. 아이디 찾기
  // db에서 사용자를 찾아서 있으면 200 응답, 없으면 409 응답 -> Optional로 컨트롤러 처리
  @GetMapping("/api/member/find-username")
  public ResponseEntity<String> findUsername(@RequestParam String email) {
    Optional<String> result = memberService.아이디찾기(email);
    if(result.isEmpty())
      return ResponseEntity.status(409).body("사용자를 찾을 수 없습니다");
    return ResponseEntity.ok(result.get());
  }


  // M-04. 임시 비밀번호를 생성해 가입한 이메일로 보낸다
  @PostMapping("/api/member/reset-password")
  public ResponseEntity<String> resetPassword(@RequestParam String username) {
    memberService.비밀번호리셋(username);
    return ResponseEntity.ok("임시비밀번호를 가입하신 이메일로 보냈습니다.");
  }

  // M-05. 내정보 보기 (비밀번호 확인을 거쳐서 와야 한다)
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/member/readme")
  public ModelAndView read(HttpSession session, Principal principal) {
    if(session.getAttribute("비밀번호_확인")==null)
      return new ModelAndView("redirect:/member/check-password");
    MemberResponseDto dto = memberService.read(principal.getName());
    return new ModelAndView("member/readme").addObject("member", dto);
  }

  // M-06. 비밀번호 확인 (이미 비밀번호를 확인했으면 내정보 보기로 간다)
  @GetMapping("/member/check-password")
  public ModelAndView checkPassword(HttpSession session) {
    if(session.getAttribute("비밀번호_확인")!=null)
      new ModelAndView("redirect:/member/read");
    return new ModelAndView("member/check-password");
  }

  // 비밀번호 확인 후 성공하면 /member/readme로, 실패하면 /member/check-password
  @PostMapping("/member/check-password")
  public String checkPassword(@RequestParam @NotEmpty String password, HttpSession session, Principal principal, RedirectAttributes ra) {
    boolean result = memberService.checkPassword(password, principal.getName());
    if(result) {
      session.setAttribute("비밀번호_확인", true);
      return "redirect:/member/read";
    } else {
      // 1회성 에러 메시지를 가지고 이동하자
      // RedirectAttribute는 이동한 다음 출력할 수 있는 값이고 자동으로 사라진다
      ra.addFlashAttribute("msg", "비밀번호를 확인하지 못했습니다");
      return "redirect:/member/check-password";
    }
  }

  @GetMapping("/member/login")
  public void login() {
  }
}
