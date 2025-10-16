package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.dtos.user.CreateUserDTO;
import dev.project.ra2avaliacao.dtos.user.UpdateUserDTO;
import dev.project.ra2avaliacao.dtos.user.UserResponseDTO;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO create(CreateUserDTO createUserDTO) {
        User existingUser = userRepository.findByEmail(createUserDTO.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User.UserBuilder()
                .name(createUserDTO.getName())
                .email(createUserDTO.getEmail())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    public UserResponseDTO getById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::convertToResponseDTO).orElse(null);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO update(String id, UpdateUserDTO updateUserDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if (updateUserDTO.getName() != null && !updateUserDTO.getName().trim().isEmpty()) {
            user.setName(updateUserDTO.getName());
        }

        if (updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().trim().isEmpty()) {
            if (!user.getEmail().equals(updateUserDTO.getEmail())) {
                User existingUser = userRepository.findByEmail(updateUserDTO.getEmail());
                if (existingUser != null) {
                    throw new RuntimeException("Email already exists");
                }
            }
            user.setEmail(updateUserDTO.getEmail());
        }

        User updatedUser = userRepository.save(user);

        return convertToResponseDTO(updatedUser);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
