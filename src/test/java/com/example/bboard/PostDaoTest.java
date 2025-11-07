package com.example.bboard;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.time.*;
import java.util.*;

@SpringBootTest
public class PostDaoTest {
  @Autowired
  private PostDao postDao;

  //@Test
  public void findAllTest() {
    List<Map<String, Object>> list = postDao.findAll1();
    for(Map<String,Object> map:list) {
      System.out.println(map.get("PNO") + " " + map.get("CNT"));
    }
  }

  //@Test
  public void findAll2Test() {
    postDao.findAll2().forEach(dto-> System.out.println(dto));
  }

  //@Test
  public void insertTest() {
    for(int i=1; i<123; i++) {
      Post p = new Post(0, i+"번째글", i+"번째 글", "spring", LocalDateTime.now(), 0, 0, 0);
      postDao.insert(p);
    }
  }

  //@Test
  public void findAllTestTest() {
    postDao.findAll(0, 5).stream().forEach(a-> System.out.println(a));
  }

  @Test
  public void findByPnoTest() {
    System.out.println(postDao.findByPno(107));
  }
}










