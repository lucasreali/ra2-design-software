package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.models.ParticipantRole;
import dev.project.ra2avaliacao.models.ProjectParticipant;
import dev.project.ra2avaliacao.repositories.ProjectParticipantRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProjectPermissionValidator {

    private final ProjectParticipantRepository projectParticipantRepository;

    public ProjectPermissionValidator(ProjectParticipantRepository projectParticipantRepository) {
        this.projectParticipantRepository = projectParticipantRepository;
    }

    public boolean isParticipant(String projectId, String userId) {
        return projectParticipantRepository.findByProjectIdAndUserId(projectId, userId).isPresent();
    }

    public boolean hasRole(String projectId, String userId, ParticipantRole role) {
        Optional<ProjectParticipant> participant = projectParticipantRepository.findByProjectIdAndUserId(projectId, userId);
        return participant.isPresent() && participant.get().getRole() == role;
    }

    public boolean isCreator(String projectId, String userId) {
        return hasRole(projectId, userId, ParticipantRole.CREATOR);
    }

    public boolean isAdmin(String projectId, String userId) {
        return hasRole(projectId, userId, ParticipantRole.ADMIN);
    }

    public boolean isMember(String projectId, String userId) {
        return hasRole(projectId, userId, ParticipantRole.MEMBER);
    }
}
