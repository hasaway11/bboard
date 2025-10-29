package com.example.bboard;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

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
}
