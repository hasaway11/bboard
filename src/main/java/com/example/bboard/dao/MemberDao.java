package com.example.bboard.dao;

// Data Access Object : DB에 sql을 전달하고 작업 결과를 받아와 자바 타입으로 변환


import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface MemberDao {
  // 아이디가 존재하는가? true/false로 리턴
  @Select("select count(*) from member where username=#{username} and rownum=1")
  boolean existsByUsername(String username);

  // 회원 가입 : 아이디, 비밀번호, 이메일, 프사를 저장
  @Insert("insert into member(username, password, email, profile) values(#{username}, #{password}, #{email}, #{profile})")
  long insert(MemberJoinDto member);

  // 사용자 정보 읽기
  @Select("select * from member where username=#{username}")
  Optional<Member> findByUsername(String username);

  // 이메일로 아이디 읽기 - 아이디 찾기에서 사용
  @Select("select username from member where email=#{email}")
  Optional<String> findUsernameByEmail(String email);

  // 비밀번호 업데이트 - 비밀번호 찾기, 비밀번호 변경
  @Update("update member set password=#{password} where username=#{username}")
  long updatePassword(String password, String username);

  @Update("update member set profile=#{profile} where username=#{username}")
  int updateProfile(String profile, String username);

  @Delete("delete from member where username=#{username}")
  int delete(String username);

  @Update("update member set failed_attempts=failed_attempts+1 where username=#{username}")
  int increaseFailedAttempts(String username);

  @Update("update member set failed_attempts=5, is_lock=1 where username=#{username}")
  int lock(String username);

  @Update("update member set failed_attempts=0 where username=#{username}")
  int resetFailedAttempts(String username);
}
