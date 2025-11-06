package com.example.bboard.dao;

import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface PostDao {
  List<Map<String, Object>> findAll1();

  List<FindAllDto> findAll2();

  @SelectKey(statement="select post_seq.nextval from dual", keyProperty="pno", before=true, resultType=long.class)
  @Insert("insert into post values(#{pno}, #{title}, #{content}, #{writer}, sysdate, 0, 0, 0)")
  int insert(Post post);

  List<PostDto.ListResponse> findAll(long start, long pagesize);

  @Select("select count(*) from post")
  long count();
}
