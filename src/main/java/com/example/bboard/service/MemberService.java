package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
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
    // 1. 파일을 이동
    // 2. 비밀번호를 암호화
    // 3. 저장
    String profile = dto.getProfile();

    // 프로필의 확장자를 자른다 : "a.b.c.jpg"라면 .jpg를 자른다 (.을 찾아서 자른다)
    int 점의_위치 = profile.lastIndexOf(".");
    String ext = profile.substring(점의_위치);
    String 저장_프로필_이름 = dto.getUsername() + ext;

    File 임시_폴더에_저장된_프로필_파일 = new File(TEMP_FOLDER_NAME, profile);
    File 새로_저장할_프로필_파일 = new File(PROFILE_FOLDER_NAME, 저장_프로필_이름);

    // 첫번째 파일을 두번째 파일에 이동시킨다. 두번째 파일이 존재하면 덮어써라
    try {
      Files.move(임시_폴더에_저장된_프로필_파일.toPath(), 새로_저장할_프로필_파일.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch(IOException e) {
      // 에러 메시지 찍어라 -> 보고 대응하자
      e.printStackTrace();
    }

    String 암호화된_비밀번호 = encoder.encode(dto.getPassword());
    dto.init(암호화된_비밀번호, 저장_프로필_이름);
    memberDao.insert(dto);
  }
}
