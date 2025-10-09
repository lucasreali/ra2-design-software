package dev.project.ra2avaliacao.dtos.project;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddParticipantDTO {
    @NotBlank(message = "User ID is required")
    private String userId;
}

