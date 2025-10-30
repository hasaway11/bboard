package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;

  //Job-M01
  public boolean usernameAvailable(String username) {
    return !memberDao.existsByUsername(username);
  }

  //Job-M02
  public void join(MemberJoinDto dto) {

  }
}
