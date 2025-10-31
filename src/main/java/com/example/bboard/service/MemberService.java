package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.io.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private PasswordEncoder encoder;

  // Job-M01 아이디 사용여부 확인
  public boolean usernameAvailable(String username) {
    return !memberDao.existsByUsername(username);
  }

  // Job-M02. 회원 가입
  // - 파일을 업로드할 때는 파일 이름이 겹치지 않게
  //      a. 이름이 겹친다면 변경한다 -> 운영체제의 방식
  //      b. 이름이 겹치지 않게 미리 바꾼다 -> 프사니까 아이디를 파일명으로 하자

  // - 프사를 업로드하지 않았다 -> 기본프사를 사용 -> 기본 프사를 개인용 프사로 복사하자 (기본 프사는 삭제 X)
  // - 프사를 업로드했다 -> 업로드 프사를 개인용 프사로 이동하자 (업로드 프사는 삭제 O)
  public void join(MemberJoinDto dto) {
    boolean 기본프사_사용 = false;

    // 1. 기본프사를 사용해야 하는 경우를 필터링
    //    dto에서 꺼낸 프로필은 프사가 아니라 업로드한 프사의 이름(문자열)이다
    //    프사를 업로드한 경우 파일명, 업로드하지 않은 경우 empty()
    String profile = dto.getProfile();
    File source = null;
    // 1-1. 프사를 업로드하지 않은 경우

    if(profile==null || profile.isEmpty()) {
      기본프사_사용 = true;
    } else {
    // 1-2. 프사를 업로드했는데 파일이 없는 경우
       source = new File(BConstant.TEMP_FOLDER_NAME, profile);
      if (source.exists() == false) {
        기본프사_사용 = true;
      }
    }

    // 2. 기본프사_사용인 경우 기본프사 열기
    if(기본프사_사용) {
      profile = "default.webp";
      source = new File(BConstant.PROFILE_FOLDER_NAME, profile);
    }

    // 3. 회원이 사용할 프사(source)를 dest로 복사하자
    // - 업로드한 경우 temp에서 profile로 복사한 다음 원본 삭제
    // - 기본 프사인 경우 복사만

    // 3.1  저장할 파일이름을 만들자 : 아이디 + 원본의 확장자
    // - 확장자를 자르자 : aaa.jpg라면 .을 찾아서 .jpg를 자르자
    int 점의_위치 = profile.lastIndexOf(".");
    String 확장자 = profile.substring(점의_위치);
    String 저장_파일명 = dto.getUsername() + 확장자;
    File dest = new File(BConstant.PROFILE_FOLDER_NAME, 저장_파일명);

    // 3.2 복사 -> 업로드_프사를 사용한 경우 삭제
    try {
      FileCopyUtils.copy(source, dest);
      if(기본프사_사용==false) {
        source.delete();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    String 암호화된_비밀번호= encoder.encode(dto.getPassword());
    dto.setPassword(암호화된_비밀번호);
    dto.setProfile(profile);
    memberDao.insert(dto);
  }


}














