package com.example.bboard.dao;

// Data Access Object : DB에 sql을 전달하고 작업 결과를 받아와 자바 타입으로 변환


import com.example.bboard.entity.*;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberDao {
  @Insert("insert into member(username, password, email, profile) values(#{username}, #{password}, #{email}, #{profile})")
  long insert(Member member);

  @Select("select * from member where username=#{username}")
  Member findByUsername(String username);
}
