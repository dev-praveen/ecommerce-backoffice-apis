package com.praveen.jpa.service;

import com.praveen.jpa.dao.AppUserRepository;
import com.praveen.jpa.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

  private final AppUserRepository userRepo;
  private final PasswordEncoder passwordEncoder;

  public void register(String username, String rawPassword) {
    AppUser user = new AppUser();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(rawPassword));
    user.setAuthorities("read");
    userRepo.save(user);
  }
}
