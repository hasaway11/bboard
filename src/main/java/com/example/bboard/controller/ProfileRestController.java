package com.example.bboard.controller;

import jakarta.annotation.*;
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
  private String TEMP_FOLDER_NAME = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "temp" + File.separator;
  private String PROFILE_FOLDER_NAME = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "profile" + File.separator;

  @PostConstruct
  public void 폴더생성() {
    // 자바의 파일 클래스는 폴더나 파일을 담당 -> File을 이용해 폴더가 없을 경우 생성
    File uploadFolder = new File(System.getProperty("user.dir") + File.separator + "upload");
    File tempFolder = new File(TEMP_FOLDER_NAME);
    File profileFolder = new File(PROFILE_FOLDER_NAME);
    if(uploadFolder.exists()==false)
      uploadFolder.mkdir();
    if(tempFolder.exists()==false)
      tempFolder.mkdir();
    if(profileFolder.exists()==false)
      profileFolder.mkdir();
  }

  // Job-P01. 프로필 사진 업로드 -> temp에 저장 후 사진을 볼수 있는 주소로 응답
  // 1. 이미지가 업로드되었는 지 확인 : profile!=null && profile.isEmpty()==false
  // 2. 이미지를 저장할 파일을 생성 : new File(TEMP_FOLDER_NAME, profile.getOriginalFilename())
  // 3. 메모리에 있는 이미지를 파일에 이동해 저장
  // 4. 이미지 주소와 이미지 이름을 Map에 담아 리턴
  @PostMapping("/api/profile/new")
  public ResponseEntity<Map<String,Object>> 프로필_업로드(MultipartFile profile) {
    try {
      if(profile!=null && profile.isEmpty()==false) {
        File dest = new File(TEMP_FOLDER_NAME, profile.getOriginalFilename());
        profile.transferTo(dest);
      }
    } catch(IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(409).body(null);
    }

    String profileName = profile.getOriginalFilename();
    String profileUrl = "/api/temp/profile?profile=" + profile.getOriginalFilename();
    Map<String, Object> map = Map.of("profileUrl", profileUrl, "profileName", profileName);
    return ResponseEntity.ok(map);
  }

  // Job-P02. 주소를 받아서 사진(byte 덩어리) + 파일 종류(contentType)를 응답
  // 1. 프로필 이름 을 파라미터로 받는다
  // 2. 임시 폴더 이름 + 프로필 이름으로 파일을 생성
  // 3. 파일을 byte배열로 변환
  // 4. contentType추가 ("image/jpeg", "image/png")
  @GetMapping("/api/temp/profile")
  public ResponseEntity<byte[]> 프사_출력(@RequestParam String profile) {
    try {
      File file = new File(TEMP_FOLDER_NAME, profile);
      byte[] imageBytes = Files.readAllBytes(file.toPath());
      // mime : 파일의 종류를 문자열로 표준 -> 이메일 첨부파일의 종류를 알려주기 위해 만들어짐
      String mimeType = "image/jpeg";
      if(profile.endsWith(".png"))
        mimeType = "image/png";
      else if(profile.endsWith(".gif"))
        mimeType = "image/gif";
      // mimeType을 자바의 MediaType으로 변환
      MediaType type = MediaType.parseMediaType(mimeType);
      return ResponseEntity.ok().contentType(type).body(imageBytes);
    } catch(IOException e) {
      return ResponseEntity.status(409).body(null);
    }
  }
}
