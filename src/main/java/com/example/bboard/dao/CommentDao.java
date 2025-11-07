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
}
