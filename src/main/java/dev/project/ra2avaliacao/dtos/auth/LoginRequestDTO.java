package dev.project.ra2avaliacao.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must have a valid format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}

