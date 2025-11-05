package com.example.bboard.security;

import com.example.bboard.dao.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import java.io.*;

@Component
public class BBoardLoginSuccessHandler implements AuthenticationSuccessHandler {
  @Autowired
  private MemberDao memberDao;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String username = request.getParameter("username");
    memberDao.resetFailedAttempts(username);
    response.sendRedirect("/");
  }
}
