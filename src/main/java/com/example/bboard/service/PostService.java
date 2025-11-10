package com.example.bboard.service;

import com.example.bboard.dao.*;
import com.example.bboard.dto.*;
import com.example.bboard.entity.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.interceptor.*;

import java.util.*;

@Service
public class PostService {
  @Autowired
  private PostDao postDao;
  @Autowired
  private CommentDao commentDao;

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

  // 현재 메소드를 분리 불가능한 트랜잭션으로 지정
  @Transactional
  public void delete(Long pno, String loginId) {
    // !!!!! 만약 댓글을 삭제한 다음 글을 삭제하려다가 오류가 발생하면 어떻게 하지 !!!!!
    // 트랜잭션(transaction) : 사람이 생각하는 하나의 작업 (dao에서는 여러 작업이 될 수 있다)
    //                        글 삭제 트랜잭션은 댓글 삭제 + 글 삭제로 구성
    //                        트랙잭션을 구성하는 sql은 모두 성공(commit) or 모두 실패(rollback) -> 부분 성공되어서는 안된다
    commentDao.deleteByPno(pno);
    if(postDao.deleteByPnoAndLoginId(pno, loginId) == 0) {
      // 예외를 발생시키지 말고 수동으로 롤백
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      // 정상 종료
      return;
    }
  }

  public void update(PostDto.UpdateRequest dto, String loginId) {
    PostDto.PostResponse search = postDao.findByPno(dto.getPno());
    if(search.getWriter().equals(loginId)) {
      Post post = dto.toEntity();
      postDao.update(post);
    }
  }
}
