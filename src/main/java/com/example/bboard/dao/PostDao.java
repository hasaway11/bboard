package com.example.bboard.dao;

import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface PostDao {
  List<Map<String, Object>> findAll1();

  List<FindAllDto> findAll2();
}
