package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.validation.constraints.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;

import java.io.*;
import java.util.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private PasswordEncoder encoder;
  // application.properties의 이메일 설정을 가지고 메일보내는 객체를 생성해 주입해준다
  @Autowired
  private JavaMailSender sender;

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
      profile = BConstant.DEFAULT_PROFILE;
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

  private void 메일보내기(String from, String to, String subject, String text) {
    MimeMessage message = sender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(text, true);
      sender.send(message);
    } catch(MessagingException e) {
      // 메일발송에 실패한 경우....but
      // - 우리 프로젝트에서 직접 메일을 보내는 것이 아니라 gmail에게 요청하면 gmail이 메일을 보내는 방식
      // - 메일 발송한 경우 그 정보는 gmail에 저장된다...개발자는 예외처리 불가
    }
  }

  public Optional<String> 아이디찾기(String email) {
    return memberDao.findUsernameByEmail(email);
  }

  public void 비밀번호리셋(String username) {
    // 사용자를 찾는다
    // 사용자가 없으면 작업 실패, 있으면 임시비밀번호 생성 -> 암호화 -> 저장 -> 메일발송
    Member member = memberDao.findByUsername(username).orElseThrow(()->new NoSuchElementException("사용자를 찾을 수 없습니다"));
    String 임시비밀번호 = RandomStringUtils.secure().nextAlphanumeric(10);
    String encodedPassword = encoder.encode(임시비밀번호);
    memberDao.updatePassword(encodedPassword, username);
    String text = "<p>임시비밀번호 : <b>" + 임시비밀번호 + "</b></p>";
    메일보내기("admin@bboard.com", member.getEmail(), "임시비밀번호", text);
  }

  // 경우의 수 3가지 : 사용자가 없다, 비밀번호가 틀리다, 비밀번호가 맞다
  public boolean checkPassword(String password, String username) {
    Member m = memberDao.findByUsername(username).orElseThrow(()->new NoSuchElementException("사용자를 찾을 수 없습니다"));
    return encoder.matches(password, m.getPassword());
  }

  public MemberResponseDto read(String username) {
    Member m = memberDao.findByUsername(username).orElseThrow(()->new NoSuchElementException("사용자를 찾을 수 없습니다"));
    return m.toResponseDto();
  }

  // 못 찾았다 -> 비밀번호가 틀리다 -> 정상 처리
  public void changePassword(ChangePasswordRequestDto dto, String username) {
    Member m = memberDao.findByUsername(username).orElseThrow(()->new NoSuchElementException("사용자를 찾을 수 없습니다"));
    if(encoder.matches(dto.getCurrentPassword(), m.getPassword()))
      throw new RuntimeException("비밀번호를 확인할 수 없습니다");
    String newEncodedPassword = encoder.encode(dto.getNewPassword());
    memberDao.updatePassword(newEncodedPassword, username);
  }
}














