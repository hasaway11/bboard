package com.example.bboard.controller;

import com.example.bboard.dto.*;
import com.example.bboard.service.*;
import jakarta.validation.constraints.*;
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

  // 글읽기는 로그인 여부와 상관없이 가능하다고 하자
  // - 로그인했고 자신이 작성하지 않은 글의 경우 조회수를 증가
  // - 서비스로 로그인 아이디를 넘겨야 하는데 Principal이 null일 수도 있다
  @GetMapping("/member/read")
  public ModelAndView read(@RequestParam @NotNull Long bno, Principal principal) {
    String loginId = principal==null? null : principal.getName();
    PostDto.PostResponse dto = postService.read(bno, loginId);
    return new ModelAndView("member/read").addObject("post", dto);
  }
}
