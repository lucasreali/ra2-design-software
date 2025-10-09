package dev.project.ra2avaliacao.dtos.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDTO {
    private String id;
    private String name;
    private String description;
    private String userId;
    private String userName;
    private String userEmail;
    private List<ParticipantResponseDTO> participants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
