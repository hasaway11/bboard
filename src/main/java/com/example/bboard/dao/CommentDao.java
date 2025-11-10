package com.example.bboard.dao;

import com.example.bboard.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface CommentDao {
  @Insert("insert into comments values(comments_seq.nextval, #{content}, #{writer}, sysdate, #{pno})")
  int insert(String content, String writer, long pno);

  @Select("select * from comments where pno=#{pno} order by pno desc")
  List<Comment> findByPno(long pno);

  @Delete("delete from comments where cno=#{cno} and writer=#{loginId}")
  int deleteByCnoAndLoginId(long cno, String loginId);

  @Delete("delete from comments where pno=#{pno}")
  int deleteByPno(Long pno);
}
