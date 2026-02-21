package com.gerolamo.authservice.controller;

import com.gerolamo.authservice.dto.AuthRequestDTO;
import com.gerolamo.authservice.dto.AuthResponseDTO;
import com.gerolamo.authservice.service.AuthService;
import com.gerolamo.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    private final UserService userService;

    public AuthController(AuthService authService,
                          UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        String token = authService.authenticate(authRequestDTO);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

}
