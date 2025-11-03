package com.example.bboard.controller;

import com.example.bboard.service.*;
import jakarta.annotation.*;
import jakarta.validation.constraints.*;
import org.apache.coyote.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// @Controller : MVC + REST
// @RestController : REST 전용
@RestController
public class ProfileRestController {

  // 스프링 프레임워크에서 객체 생성은 스프링이 담당 -> 생성자는 스프링이 사용해야 한다
  // 개발자에게는 생성자대신 사용할 초기화 함수가 필요 -> 함수 대신 어노테이션이 정해져 있다
  @PostConstruct
  public void 폴더생성() {
    // 자바의 파일 클래스는 폴더나 파일을 담당 -> File을 이용해 폴더가 없을 경우 생성
    File uploadFolder = new File(BConstant.UPLOAD_FOLDER_NAME);
    File tempFolder = new File(BConstant.TEMP_FOLDER_NAME);
    File profileFolder = new File(BConstant.PROFILE_FOLDER_NAME);
    if(uploadFolder.exists()==false)
      uploadFolder.mkdir();
    if(tempFolder.exists()==false)
      tempFolder.mkdir();
    if(profileFolder.exists()==false)
      profileFolder.mkdir();
  }

  // Job-P01. 프로필 사진 업로드 -> temp에 저장 후 사진을 볼수 있는 주소로 응답
  @PostMapping("/api/profile/new")
  public ResponseEntity<Map<String,Object>> 프로필_업로드(MultipartFile profile) {
    // 업로드한 파일은 MultipartFile로 받는다 -> MultipartFile은 메모리에 떠 있는 상태 -> 파일에 저장해야 한다

    // 프사를 업로드했는 지 확인 -> File 객체 생성 -> MultipartFile을 파일로 이동
    try {
      if(profile!=null && profile.isEmpty()==false) {
        File dest = new File(BConstant.TEMP_FOLDER_NAME, profile.getOriginalFilename());
        // 회원 가입할 때는 FileCopyUtils.copy()를 사용했는데?
        // - 기본프사를 사용한 경우에는 원본을 지우면 안되니까 복사했다
        profile.transferTo(dest);
      }
    } catch(IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(409).body(null);
    }

    // 프로필 이미지를 출력하는 url과 프로필 이름을 Map에 담아 리턴
    // "/api/temp/profile?profile=spring.jpg" + "spring.jpg"
    String profileName = profile.getOriginalFilename();
    String profileUrl = "/api/profile-image/profile?profile=" + profile.getOriginalFilename();
    Map<String, Object> map = Map.of("profileUrl", profileUrl, "profileName", profileName);
    return ResponseEntity.ok(map);
  }

  // binary(문자열이 아닌 데이터) 데이터를 리턴할 때는 데이터 종류를 함께 보내줘야 한다
  private ResponseEntity<byte[]> readProfile(String folder, String profile) {
    try {
      // 1. 파일을 byte 배열로 읽어오기
      File file = new File(folder, profile);
      byte[] imageBytes = Files.readAllBytes(file.toPath());

      // 2. 파일의 확장자를 이용해 데이터의 종류(ContentType) 지정하기
      String mimeType = "image/jpeg";
      profile = profile.toLowerCase();
      if(profile.endsWith(".png"))
        mimeType = "image/png";
      else if(profile.endsWith(".gif"))
        mimeType = "image/gif";
      else if(profile.endsWith(".webp"))
        mimeType = "image/webp";
      MediaType type = MediaType.parseMediaType(mimeType);

      // 3. 데이터와 ContentType으로 ResponseEntity를 리턴
      return ResponseEntity.ok().contentType(type).body(imageBytes);
    } catch(IOException e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(409).body(null);
  }

  @GetMapping("/api/profile-image/profile")
  public ResponseEntity<byte[]> 미리보기(@RequestParam @NotEmpty String profile) {
    return readProfile(BConstant.TEMP_FOLDER_NAME, profile);
  }

  @GetMapping("/api/profile-image/temp")
  public ResponseEntity<byte[]> 가입할때_미리보기(@RequestParam @NotEmpty String profile) {
    return readProfile(BConstant.PROFILE_FOLDER_NAME, profile);
  }
}
