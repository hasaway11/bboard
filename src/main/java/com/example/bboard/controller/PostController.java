package com.example.bboard.controller;

import com.example.bboard.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

@Controller
public class PostController {
  @Autowired
  private PostService postService;

  @GetMapping("/")
  public ModelAndView list() {
    return new ModelAndView("post/find-all1").addObject("posts", postService.findAll1());
  }
}
