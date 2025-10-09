package dev.project.ra2avaliacao.controllers;

import dev.project.ra2avaliacao.dtos.auth.LoginRequestDTO;
import dev.project.ra2avaliacao.dtos.auth.LoginResponseDTO;
import dev.project.ra2avaliacao.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO session = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
