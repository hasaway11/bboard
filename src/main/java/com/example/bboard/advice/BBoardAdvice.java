package com.example.bboard.advice;

import jakarta.servlet.http.*;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.servlet.*;

import java.util.*;

// @GetMapping, @PostMapping등 url을 처리하는 함수를 모아놓은 것이 Controller(정상 처리 담당)
// 예외를 처리하는 @ExceptionHandler를 모아놓은 클래스가 ControllerAdvice(예외 처리 담당)

@ControllerAdvice
public class BBoardAdvice {
  // DTO 검증이 실패한 경우 MethodArgumentNotValidException
  // @RequestParam 검증이 실패한 경우 HandlerMethodValidationException
  @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class})
  public Object 검증실패_handler(HttpServletRequest req) {
    // 요청, 응답 객체는 내용(body)과 메타 정보(header)로 구성
    // 요청 객체의 헤더 정보를 이용해서 ajax 여부를 판단하자
    // ajax 요청일 때 어떤 헤더가 있는지는 사용하는 ajax 라이브러리에 따라 다르다

    // axios의 경우
    String accept = req.getHeader("Accept");
    // jQuery의 경우
    String xhr = req.getHeader("X-Requestd_With");
    boolean isAjax = (xhr!=null && xhr.equalsIgnoreCase("XMLHttpRequest"))
        || (accept!=null && accept.contains("application/json"));
    if(isAjax==true) {
      return ResponseEntity.status(409).body("잘못된 요청입니다");
    } else {
      return new ModelAndView("error").addObject("msg", "잘못된 요청입니다");
    }
  }

  @ExceptionHandler(NoSuchElementException.class)
  public Object 검색에_실패한_경우_handler(HttpServletRequest req, NoSuchElementException e) {
    String accept = req.getHeader("Accept");
    String xhr = req.getHeader("X-Requestd_With");
    boolean isAjax = (xhr!=null && xhr.equalsIgnoreCase("XMLHttpRequest"))
        || (accept!=null && accept.contains("application/json"));
    if(isAjax==true) {
      return ResponseEntity.status(409).body(e.getMessage());
    } else {
      return new ModelAndView("error").addObject("msg", e.getMessage());
    }
  }
}
