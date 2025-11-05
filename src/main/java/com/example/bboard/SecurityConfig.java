package com.example.bboard;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

// @PreAuthorize(로그인 여부), @Secured(권한 체크)를 활성화
@EnableMethodSecurity(securedEnabled = true)
// 스프링 설정용 빈
@Configuration
public class SecurityConfig {
  @Bean
  PasswordEncoder passwordEncoder() {
    // BCrypt는 비밀번호 암호화를 위해 개발된 알고리즘
    return new BCryptPasswordEncoder();
  }

  @Autowired
  private AuthenticationFailureHandler failureHandler;
  @Autowired
  private AuthenticationSuccessHandler successHandler;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity config) throws Exception {
    // csrf(뷰페이지 위변조를 눈치채는 기능)를 끈다
    config.csrf(c->c.disable());
    // 로그인 처리 주소, 로그아웃 주소를 변경
    config.formLogin(c-> c.loginPage("/member/login").loginProcessingUrl("/member/login")
        .failureHandler(failureHandler).successHandler(successHandler));
    config.logout(c-> c.logoutUrl("/member/logout").logoutSuccessUrl("/"));

    return config.build();
  }
}
