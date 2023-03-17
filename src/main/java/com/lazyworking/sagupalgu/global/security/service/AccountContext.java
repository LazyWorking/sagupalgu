package com.lazyworking.sagupalgu.global.security.service;

import com.lazyworking.sagupalgu.user.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
public class AccountContext extends org.springframework.security.core.userdetails.User {
  private User user;

  public AccountContext(User user, List<GrantedAuthority> roles) {
    super(user.getName(), user.getPassword(), roles);
    this.user = user;
  }
}
