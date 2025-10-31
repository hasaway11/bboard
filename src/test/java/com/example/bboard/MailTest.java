package com.example.bboard;

import com.example.bboard.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
public class MailTest {
  @Autowired
  private MemberService service;

  @Test
  public void 메일테스트() {
    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    service.메일보내기();
  }
}
