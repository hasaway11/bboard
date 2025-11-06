package com.example.bboard.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.access.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.access.*;
import org.springframework.stereotype.*;

import java.io.*;

// 403 처리
@Component
public class BBoardAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    String 요청주소 = request.getRequestURI();
    if(요청주소.startsWith("/api/")) {
      response.setStatus(403);
    } else {
      response.sendRedirect("/member/login");
    }
  }
}









