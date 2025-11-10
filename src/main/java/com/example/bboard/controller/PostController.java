package com.example.bboard.controller;

import com.example.bboard.dto.*;
import com.example.bboard.service.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;

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
  @GetMapping("/post/read")
  public ModelAndView read(@RequestParam @NotNull Long pno, Principal principal) {
    String loginId = principal==null? null : principal.getName();
    PostDto.PostResponse dto = postService.read(pno, loginId);
    return new ModelAndView("post/read").addObject("post", dto)
        .addObject("isLogin", principal!=null)
        .addObject("isWriter", dto.getWriter().equals(loginId));
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/post/delete")
  public String delete(@RequestParam @NotNull Long pno, Principal principal) {
    postService.delete(pno, principal.getName());
    return "redirect:/";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/post/update")
  public ModelAndView update(@RequestParam @NotNull Long pno, Principal principal) {
    PostDto.PostResponse dto = postService.read(pno, principal.getName());
    return new ModelAndView("post/update").addObject("post", dto);
  }

  @Secured("ROLE_USER")
  @PatchMapping("/api/post")
  public ResponseEntity<Void> update(@ModelAttribute @Valid PostDto.UpdateRequest dto, Principal principal) {
    postService.update(dto, principal.getName());
    return ResponseEntity.ok(null);
  }

}
