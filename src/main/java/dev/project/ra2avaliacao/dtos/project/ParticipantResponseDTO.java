package dev.project.ra2avaliacao.dtos.project;

import dev.project.ra2avaliacao.models.ParticipantRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantResponseDTO {
    private String id;
    private String userId;
    private String userName;
    private String userEmail;
    private ParticipantRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

