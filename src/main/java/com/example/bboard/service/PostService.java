package com.example.bboard.service;

import com.example.bboard.dao.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class PostService {
  @Autowired
  private PostDao postDao;

  public List<Map<String, Object>> findAll1() {
    return postDao.findAll1();
  }
}
