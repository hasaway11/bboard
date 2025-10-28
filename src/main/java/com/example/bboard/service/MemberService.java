package com.example.bboard.service;

import com.example.bboard.dao.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;

  //Job-M01
  public boolean usernameAvailable(String username) {
    return !memberDao.existsByUsername(username);
  }
}
