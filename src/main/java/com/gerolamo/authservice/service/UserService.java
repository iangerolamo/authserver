package com.gerolamo.authservice.service;

import com.gerolamo.authservice.dto.RegisterRequestDTO;
import com.gerolamo.authservice.dto.RegisterResponseDTO;
import com.gerolamo.authservice.model.Role;
import com.gerolamo.authservice.model.User;
import com.gerolamo.authservice.repository.RoleRepository;
import com.gerolamo.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    public UserService(RoleRepository roleRepository,
                       UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {

        String email = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER").get();

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setEnabled(true);
        user.addRole(defaultRole);

        User savedUser = userRepository.save(user);

        List<String> roles = savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();

        registerResponseDTO.setName(savedUser.getName());
        registerResponseDTO.setEmail(savedUser.getEmail());

        return registerResponseDTO;
  }
}
