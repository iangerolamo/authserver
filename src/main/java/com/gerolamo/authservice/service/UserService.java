package com.gerolamo.authservice.service;

import com.gerolamo.authservice.dto.RegisterRequestDTO;
import com.gerolamo.authservice.dto.RegisterResponseDTO;
import com.gerolamo.authservice.model.Role;
import com.gerolamo.authservice.model.User;
import com.gerolamo.authservice.repository.RoleRepository;
import com.gerolamo.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(RoleRepository roleRepository,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        String email = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }

        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Role padrao nao encontrada: " + DEFAULT_ROLE));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.addRole(defaultRole);

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    private RegisterResponseDTO mapToResponse(User user) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }
}
