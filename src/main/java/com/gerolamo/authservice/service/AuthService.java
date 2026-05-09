package com.gerolamo.authservice.service;

import com.gerolamo.authservice.dto.AuthRequestDTO;
import com.gerolamo.authservice.model.User;
import com.gerolamo.authservice.repository.UserRepository;
import com.gerolamo.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(AuthRequestDTO request) {
        String email = request.getUsername().toLowerCase().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao cadastrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
