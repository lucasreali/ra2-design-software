package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.dtos.auth.LoginResponseDTO;
import dev.project.ra2avaliacao.dtos.user.UserResponseDTO;
import dev.project.ra2avaliacao.models.Session;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.repositories.SessionRepository;
import dev.project.ra2avaliacao.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionRepository = sessionRepository;
    }

    public LoginResponseDTO login(String email, String password) {
        User user = userRepository.findByEmail(email);


        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        System.out.println(user.getEmail());


        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusDays(7));

        return convertToResponseDTO(sessionRepository.save(session));
    }

    private LoginResponseDTO convertToResponseDTO(Session session) {
        LoginResponseDTO dto = new LoginResponseDTO();
        UserResponseDTO user = new UserResponseDTO();

        dto.setSessionId(session.getToken());

        user.setId(session.getUser().getId());
        user.setName(session.getUser().getName());
        user.setEmail(session.getUser().getEmail());
        user.setCreatedAt(session.getUser().getCreatedAt());
        user.setUpdatedAt(session.getUser().getUpdatedAt());

        dto.setUser(user);
        return dto;
    }
}
