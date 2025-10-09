package dev.project.ra2avaliacao.dtos.auth;

import dev.project.ra2avaliacao.dtos.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String sessionId;
    private UserResponseDTO user;
}

