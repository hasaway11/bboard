package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CommentService {
  @Autowired
  private CommentDao commentDao;

  public List<Comment> write(String content, String writer, long pno) {
    commentDao.insert(content, writer, pno);
    return commentDao.findByPno(pno);
  }
}
