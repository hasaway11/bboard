package com.example.bboard.controller;

import com.example.bboard.dto.*;
import com.example.bboard.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.security.*;
import java.util.*;

@Controller
public class PostController {
  @Autowired
  private PostService postService;

  @GetMapping("/")
  public ModelAndView list(@RequestParam(defaultValue = "1") long pageno) {
    Map<String, Object> map = postService.findAll(pageno);
    return new ModelAndView("post/list").addObject("map", map);
  }

  @Secured("ROLE_USER")
  @GetMapping("/post/write")
  public void write() {
  }

  @Secured("ROLE_USER")
  @PostMapping("/post/write")
  public ModelAndView write(@ModelAttribute PostDto.WriteRequest dto, Principal principal) {
    long pno = postService.write(dto, principal.getName());
    return new ModelAndView("redirect:/post/read?pno=" + pno);
  }
}
