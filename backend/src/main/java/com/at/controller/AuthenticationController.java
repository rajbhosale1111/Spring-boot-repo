package com.at.controller;

import com.at.dao.request.SignUpRequest;
import com.at.dao.request.SigninRequest;
import com.at.dao.response.JwtAuthenticationResponse;
import com.at.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.v1.baseUrl}/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/signup")
  public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
    return new ResponseEntity<>(authenticationService.signup(request), HttpStatus.CREATED);
  }

  @PostMapping("/signin")
  public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
    return ResponseEntity.ok(authenticationService.signin(request));
  }
}
