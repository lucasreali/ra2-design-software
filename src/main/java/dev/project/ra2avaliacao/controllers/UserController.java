package dev.project.ra2avaliacao.controllers;

import dev.project.ra2avaliacao.dtos.user.CreateUserDTO;
import dev.project.ra2avaliacao.dtos.user.UpdateUserDTO;
import dev.project.ra2avaliacao.dtos.user.UserResponseDTO;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody CreateUserDTO createUserDTO) {
        UserResponseDTO createdUser = userService.create(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UserResponseDTO user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            @Valid @RequestBody UpdateUserDTO updateUserDTO,
            @AuthenticationPrincipal User user) {
        UserResponseDTO updatedUser = userService.update(user.getId(), updateUserDTO);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal User user) {
        userService.delete(user.getId());
        return ResponseEntity.noContent().build();
    }
}
