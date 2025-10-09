package dev.project.ra2avaliacao.dtos.project;

import dev.project.ra2avaliacao.models.ParticipantRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateParticipantRoleDTO {
    @NotNull(message = "Role is required")
    private ParticipantRole role;
}

