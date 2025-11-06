package com.example.bboard.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.stereotype.*;

import java.io.*;

// 401을 처리 -> 기본구현은 로그인 페이지로 이동
//           -> ajax의 경우 화면 이동은 프론트가 해야한다 -> 그냥 403이라고 쏴주자

// 어떻게 구현? ajax요청 이면 403으로 응답 + MVC 요청이면 로그인 페이지로 이동
@Component
public class BBoardAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    String 요청주소 = request.getRequestURI();
    if(요청주소.startsWith("/api/")) {
      response.setStatus(401);
    } else {
      response.sendRedirect("/member/login");
    }
  }
}









