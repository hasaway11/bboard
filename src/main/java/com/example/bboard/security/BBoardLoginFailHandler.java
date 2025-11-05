package com.example.bboard.security;

import com.example.bboard.dao.*;
import com.example.bboard.entity.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

// ※ 로그인에 실패했을 때 실행되는 클래스
// 파라미터로 전달되는 AuthenticationException은 모든 로그인 실패의 부모
//  - BadCredentialException : 아이디나 비밀번호가 틀렸을 때 -> 로그인 실패횟수 증가 -> "로그인에 3회 실패했습니다"를 출력
//  - LockedException : 계정이 블록되었을 때 -> "블록된 계정입니다"라고 출력

// 1. request에서 사용자가 로그인 요청한 아이디를 꺼낸다 (req.getParamter("username"))
// 2. BadCredentialException인 경우
//    - 로그인 실패횟수를 읽어온다
//    - 로그인 실패횟수를 1증가시킨 다음 실패횟수가 5번이 안되면 실패횟수를 저장
//    -                                       5번이 되면 실패횟수 저장 + 계정 블록
// 3. LockedException인 경우
@Component
public class BBoardLoginFailHandler implements AuthenticationFailureHandler {
  @Autowired
  private MemberDao memberDao;

  @Override
  public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e)
      throws IOException, ServletException {
    HttpSession session = req.getSession();

    if(e instanceof BadCredentialsException) {
      String username = req.getParameter("username");
      Optional<Member> optional = memberDao.findByUsername(username);
      if(optional.isEmpty())
        session.setAttribute("msg", "아이디나 비밀번호를 잘못입력했습니다");
      else {
        Member m = optional.get();
        if(m.getFailedAttempts()<4) {
          // 로그인 실패횟수 증가
          long 로그인_실패_횟수 = m.getFailedAttempts() + 1;
          session.setAttribute("msg", "로그인에 " + 로그인_실패_횟수 + "번 실패. 5번 실패하면 계정이 블록됩니다");
          memberDao.increaseFailedAttempts(username);
        } else {
          // 로그인 실패횟수 = 5 + 계정 블록
          memberDao.lock(username);
          session.setAttribute("msg", "로그인에 5회실패해 계정이 블록되었습니다");
        }
      }
    } else if(e instanceof LockedException) {
      session.setAttribute("msg", "블록된 계정입니다. 관리자에게 연락하세요");
    }
    res.sendRedirect("/member/login");
  }
}
