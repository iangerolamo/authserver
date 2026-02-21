package com.gerolamo.authservice.service;

import com.gerolamo.authservice.dto.AuthRequestDTO;
import com.gerolamo.authservice.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final JwtUtil jwtUtil;

  public AuthService(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  public String authenticate(AuthRequestDTO request) {

    return jwtUtil.generateToken(request.getUsername());
  }
}
