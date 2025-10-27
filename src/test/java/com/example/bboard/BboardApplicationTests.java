package com.example.bboard;

import com.example.bboard.dao.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BboardApplicationTests {
  @Autowired
  private MemberDao memberDao;

	@Test
	void initTest() {
    System.out.println(memberDao);
	}

}
