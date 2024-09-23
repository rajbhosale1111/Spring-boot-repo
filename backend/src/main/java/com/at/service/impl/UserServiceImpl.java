package com.at.service.impl;

import com.at.entities.User;
import com.at.repository.UserRepository;
import com.at.security.UserPrincipal;
import com.at.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDetailsService userDetailsService() {
    return username -> {
      User user = userRepository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      return new UserPrincipal(user);
    };
  }

  @Override
  public User findById(Integer id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public List<User> fetchAll() {
    return userRepository.findAll();
  }

  @Override
  public void delete(Integer id) {

  }

  @Override
  public User create(User obj) {
    return null;
  }

  @Override
  public User update(Integer id, User obj) {
    return null;
  }
}
