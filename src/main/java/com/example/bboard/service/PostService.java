package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class PostService {
  @Autowired
  private PostDao postDao;
  @Value("10")
  private long PAGE_SIZE;
  @Value("5")
  private long BLOCK_SIZE;

  // 데이터, prev, next, 페이지 번호들의 List인 pages
  public Map<String, Object> findAll(long pageno) {
    long start = (pageno-1) * PAGE_SIZE;
    List<PostDto.ListResponse> posts = postDao.findAll(start, PAGE_SIZE);

    long totalCount = postDao.count();
    long numberOfPages = (totalCount-1)/PAGE_SIZE;

    long prev = (pageno-1)/BLOCK_SIZE * BLOCK_SIZE;
    long begin = prev + 1;
    long end = prev + BLOCK_SIZE;
    long next = end + 1;

    if(end>=numberOfPages) {
      end = numberOfPages;
      next = 0;
    }

    List<Long> pages = new ArrayList<>();
    for(long i=begin; i<=end; i++)
      pages.add(i);
    System.out.println(Map.of("prev", prev, "next", next, "pages", pages, "posts", posts, "pageno", pageno));
    return Map.of("prev", prev, "next", next, "pages", pages, "posts", posts, "pageno", pageno);
  }

  public long write(PostDto.WriteRequest dto, String username) {
    Post post = dto.toEntity(username);
    postDao.insert(post);
    return post.getPno();
  }

  public PostDto.PostResponse read(long pno, String loginId) {
    PostDto.PostResponse dto = postDao.findByPno(pno);
    // 로그인했고 글쓴이가 아닌 경우 조회수 증가
    if(loginId!=null && dto.getWriter().equals(loginId)==false) {
      postDao.update(Post.builder().pno(pno).readCnt(1).build());
      dto.increaseReadCnt();
    }
    return dto;
  }
}
