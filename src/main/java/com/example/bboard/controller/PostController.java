package com.example.bboard.controller;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {
  // ModelAndView에서 View만 있을 때 String으로 처리할 수 있다
  @GetMapping("/")
  public String list() {
    return "post/list";
  }
}
