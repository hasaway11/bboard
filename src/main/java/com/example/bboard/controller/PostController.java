package com.example.bboard.controller;

import com.example.bboard.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.util.*;

@Controller
public class PostController {
  @Autowired
  private PostService postService;

  @GetMapping("/")
  public ModelAndView list(@RequestParam(defaultValue="1") long pageno) {
    Map<String, Object> map = postService.findAll(pageno);
    return new ModelAndView("post/list").addObject("map", map);
  }
}
