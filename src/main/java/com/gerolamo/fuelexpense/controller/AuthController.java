package com.gerolamo.fuelexpense.controller;

import com.gerolamo.fuelexpense.dto.AuthRequestDTO;
import com.gerolamo.fuelexpense.dto.AuthResponseDTO;
import com.gerolamo.fuelexpense.service.AuthService;
import com.gerolamo.fuelexpense.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    private final UserService userService;

    public AuthController(AuthService authService,
                          UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        try {
            String token = authService.authenticate(authRequestDTO);
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

}
