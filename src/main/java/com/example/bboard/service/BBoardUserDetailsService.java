package com.example.bboard.service;

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
    Member m = memberDao.findByUsername(username);
    return User.builder().username(m.getUsername()).password(m.getPassword()).roles(m.getRole().name()).build();
  }
}
