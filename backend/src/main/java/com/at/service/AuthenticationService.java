package com.at.service;

import com.at.dao.request.SignUpRequest;
import com.at.dao.request.SigninRequest;
import com.at.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {

  JwtAuthenticationResponse signup(SignUpRequest request);

  JwtAuthenticationResponse signin(SigninRequest request);
}
