package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.nio.file.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private PasswordEncoder encoder;
  private String TEMP_FOLDER_NAME = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "temp" + File.separator;
  private String PROFILE_FOLDER_NAME = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "profile" + File.separator;


  //Job-M01
  public boolean usernameAvailable(String username) {
    return !memberDao.existsByUsername(username);
  }

  //Job-M02
  public void join(MemberJoinDto dto) {
    boolean 기본프사_사용 = false;

    // 1. 기본프사를 사용할 지 말지를 결정
    String profile = dto.getProfile();
    File 원본_파일 = null;
    if(profile=="")
      기본프사_사용 = true;
    else {
      원본_파일 = new File(TEMP_FOLDER_NAME, profile);
      if (원본_파일.exists()==false)
        기본프사_사용 = true;
    }

    // 2. 기본_프사를 사용할 경우 원본_파일을 기본 프사로 바꾼다
    if(기본프사_사용==true)
      원본_파일 = new File(PROFILE_FOLDER_NAME, "default.webp");

    // 3. 원본_파일을 복사할 대상을 생성
    // - 프로필의 확장자를 자른다 : "a.b.c.jpg"라면 .jpg를 자른다 (.을 찾아서 자른다)
    int 점의_위치 = profile.lastIndexOf(".");
    String ext = profile.substring(점의_위치);
    String 저장_프로필_이름 = dto.getUsername() + ext;
    File 프로필_파일 = new File(PROFILE_FOLDER_NAME, 저장_프로필_이름);

    // 4. 원본_파일을 프로필_파일에 복사
    // 5. 원본_파일이 기본 프사가 아닌 경우 삭제
    try {
      FileCopyUtils.copy(원본_파일, 프로필_파일);
      if(기본프사_사용==true)
        프로필_파일.delete();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String 암호화된_비밀번호 = encoder.encode(dto.getPassword());
    dto.init(암호화된_비밀번호, 저장_프로필_이름);
    memberDao.insert(dto);
  }
}
