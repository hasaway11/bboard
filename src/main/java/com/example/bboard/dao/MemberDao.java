package com.example.bboard.dao;

// Data Access Object : DB에 sql을 전달하고 작업 결과를 받아와 자바 타입으로 변환


import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberDao {
  // Job-M01. 아이디가 존재하는가? true/false로 리턴
  @Select("select count(*) from member where username=#{username} and rownum=1")
  boolean existsByUsername(String username);

  // Job-M02
  @Insert("insert into member(username, password, email, profile) values(#{username}, #{password}, #{email}, #{profile})")
  long insert(MemberJoinDto member);

  // 로그인
  @Select("select * from member where username=#{username}")
  Member findByUsername(String username);

  @Select("select username from member where email=#{email}")
  String findUsernameByEmail(String email);

  @Update("update member set password=#{password} where username=#{username}")
  long updatePassword(String password, String username);
}
