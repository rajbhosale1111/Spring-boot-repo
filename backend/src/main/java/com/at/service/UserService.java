package com.at.service;

import com.at.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends BaseCrudService<User, Integer> {

  UserDetailsService userDetailsService();
}
