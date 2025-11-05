package com.example.bboard.security;

import com.example.bboard.dao.*;
import com.example.bboard.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Service
public class BBoardUserDetailsService implements UserDetailsService {
  @Autowired
  private MemberDao memberDao;

  // DB에서 사용자 정보를 읽어 UserDetails 형식으로 반환
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // throw : 예외 발생
    // throws : 현재 메소드에서 예외를 처리하지 않고 외부로 던져버린다 -> @ExceptionHandler
//    Optional<Member> result = memberDao.findByUsername(username);
//    if(result.isEmpty())
//      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
//    Member m = result.get();

    Member m = memberDao.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
    return User.builder().username(m.getUsername()).password(m.getPassword()).roles(m.getRole().name())
        .accountLocked(m.isLock()).build();
  }
}
