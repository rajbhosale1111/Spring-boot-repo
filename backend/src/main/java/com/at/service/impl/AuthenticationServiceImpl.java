package com.at.service.impl;

import com.at.dao.request.SignUpRequest;
import com.at.dao.request.SigninRequest;
import com.at.dao.response.JwtAuthenticationResponse;
import com.at.entities.Role;
import com.at.entities.User;
import com.at.repository.UserRepository;
import com.at.security.UserPrincipal;
import com.at.service.AuthenticationService;
import com.at.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Override
  public JwtAuthenticationResponse signup(SignUpRequest request) {
    var user = User.builder().name(request.getName()).email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
        .build();
    user = userRepository.save(user);
    var jwt = jwtService.generateToken(new UserPrincipal(user));
    return JwtAuthenticationResponse.builder().token(jwt).build();
  }

  @Override
  public JwtAuthenticationResponse signin(SigninRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
    var jwt = jwtService.generateToken(new UserPrincipal(user));
    return JwtAuthenticationResponse.builder().token(jwt).build();
  }
}
