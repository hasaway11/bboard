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
  // 1. 파일을 base64 문자열로 바꾸서 db에 저장
  //     단점 : 이미지를 문자열로 바꿔 db에 직접 저장하므로 저장 크기가 매우 커진다
  //     장점: 회원을 삭제하면 이미지도 당연히 삭제
  // 2. 파일을 디스크에 저장하고 파일이름을 db에 저장
  //     장점: 파일이름만 저장하므로 매우 가벼운 작업, 단점 : 회원을 삭제할 때 파일이 삭제되지 않는다
  public void join(MemberJoinDto dto) {
    MultipartFile profile = dto.getProfile();
    if(profile!=null && profile.isEmpty()==false) {
      // 프사 이름을 변경하지 : 프사의 파일명은 사용자 아이디로 하고 원본의 확장자를 가져다 붙이자
      //    1234.png를 업로드 ->  spring.png로 저장하자
      String 프사이름 = dto.getUsername();
      String 원본이름 = profile.getOriginalFilename();
      // 원본이름이 1234.png라면 마지막 .을 찾아서 자르자
      int 점의위치 = 원본이름.lastIndexOf(".");
      String 확장자 = 원본이름.substring(점의위치);
      System.out.println(프사이름 + 확장자);
    }
  }
}
