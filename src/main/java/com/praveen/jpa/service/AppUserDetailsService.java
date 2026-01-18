package com.praveen.jpa.service;

import com.praveen.jpa.dao.AppUserRepository;
import com.praveen.jpa.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

  private final AppUserRepository userRepo;

  @Override
  @NonNull
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

    AppUser user =
        userRepo
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return User.withUsername(user.getUsername())
        .password(user.getPassword())
        .authorities(user.getAuthorities())
        .build();
  }
}
