package com.gerolamo.authservice.service;

import com.gerolamo.authservice.dto.AuthRequestDTO;
import com.gerolamo.authservice.repository.UserRepository;
import com.gerolamo.authservice.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  public AuthService(JwtUtil jwtUtil, UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  public String authenticate(AuthRequestDTO request) {
    String email = request.getUsername().toLowerCase().trim();
    
    if (!userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Usuario nao cadastrado");
    }

    return jwtUtil.generateToken(request.getUsername());
  }
}
