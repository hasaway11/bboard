package com.example.bboard.service;

import lecture.*;

import java.io.*;

// 인터페이스는 추상 메소드나 상수를 저장할 목적으로 사용
// 인터페이스에 저장된 상수는 단일 상수
// enum을 사용하는 경우는 범위 상수(과일은 바나나 또는 사과 또는 딸기)
public interface BConstant {
  final static String DEFAULT_PROFILE = "default.webp";

  // final : 변경 금지, static : 객체없이도 사용가능 -> final + static : 어디서나 사용가능한 상수
  // System.getProperty("user.dir") : 프로젝트의 경로를 가져와라
  final static String UPLOAD_FOLDER_NAME = System.getProperty("user.dir") + File.separator + "upload" + File.separator;
  final static String TEMP_FOLDER_NAME = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "temp" + File.separator;
  final static String PROFILE_FOLDER_NAME = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "profile" + File.separator;
}