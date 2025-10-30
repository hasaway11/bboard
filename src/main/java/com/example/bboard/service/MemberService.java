package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import jakarta.validation.*;
import org.apache.commons.lang3.*;
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

    if(profile==null || profile.isEmpty()==true)
      기본프사_사용 = true;
    else {
      원본_파일 = new File(TEMP_FOLDER_NAME, profile);
      if (원본_파일.exists()==false)
        기본프사_사용 = true;
    }
    System.out.println(기본프사_사용);

    // 2. 기본_프사를 사용할 경우 원본_파일을 기본 프사로 바꾼다
    if(기본프사_사용==true)
      원본_파일 = new File(PROFILE_FOLDER_NAME, "default.webp");

    // 3. 원본_파일을 복사할 대상 객체를 생성
    // - 일단 기본 프사를 사용한다고 가정한 다음
    // - 기본 프사를 사용하지 않는 경우 파일명을 바꾼다
    String 저장_프로필_이름 = dto.getUsername() + ".webp";
    if(기본프사_사용==false) {
      int 점의_위치 = profile.lastIndexOf(".");
      String ext = profile.substring(점의_위치);
      저장_프로필_이름 = dto.getUsername() + ext;
    }
    File 프로필_파일 = new File(PROFILE_FOLDER_NAME, 저장_프로필_이름);

    // 4. 원본_파일을 프로필_파일에 복사
    // 5. 원본_파일이 기본 프사가 아닌 경우 삭제
    try {
      FileCopyUtils.copy(원본_파일, 프로필_파일);
      if(기본프사_사용==false)
        원본_파일.delete();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String 암호화된_비밀번호 = encoder.encode(dto.getPassword());
    dto.init(암호화된_비밀번호, 저장_프로필_이름);
    memberDao.insert(dto);
  }

  public String findUsername(String email) {
    return memberDao.findUsernameByEmail(email);
  }

  // 1. 사용자 정보를 읽어온다 -> 사용자가 없으면 return false
  // 2. 랜덤한 비밀번호를 생성한 다음 암호화 후 저장
  // 3. 이메일로 임시 비밀번호를 전송한다
  public boolean resetPassword(String username) {
    Member member = memberDao.findByUsername(username);
    if(member==null)
      return false;
    // pom.xml에 추가한 apache commons lang 라이브러리를 이용해 랜덤한 문자열을 생성한다
    String randomPassword = RandomStringUtils.secure().nextAlphanumeric(10);
    String 새로운_암호화_비밀번호 = encoder.encode(randomPassword);
    return memberDao.updatePassword(새로운_암호화_비밀번호, username)==1;
  }
}
