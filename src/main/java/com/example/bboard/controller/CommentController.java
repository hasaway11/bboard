package com.example.bboard.controller;

import com.example.bboard.entity.*;
import com.example.bboard.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.*;

@Controller
public class CommentController {
  @Autowired
  private CommentService commentService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/api/comment/new")
  public ResponseEntity<List<Comment>> write(@RequestParam long pno, @RequestParam String content, Principal principal) {
    List<Comment> comments = commentService.write(content, principal.getName(), pno);
    return ResponseEntity.ok(comments);
  }
}
