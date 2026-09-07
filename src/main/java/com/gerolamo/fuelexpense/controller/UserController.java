package com.gerolamo.fuelexpense.controller;

import com.gerolamo.fuelexpense.dto.RegisterRequestDTO;
import com.gerolamo.fuelexpense.dto.RegisterResponseDTO;
import com.gerolamo.fuelexpense.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {

        RegisterResponseDTO registerResponseDTO = userService.register(registerRequest);

        return ResponseEntity.ok(registerResponseDTO);
    }
}
