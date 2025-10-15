package dev.project.ra2avaliacao.strategies;

import dev.project.ra2avaliacao.models.ParticipantRole;
import dev.project.ra2avaliacao.models.Project;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.repositories.ProjectParticipantRepository;
import org.springframework.stereotype.Component;

@Component
public class CreatorPermissionStrategy implements PermissionStrategy {

    private final ProjectParticipantRepository projectParticipantRepository;

    public CreatorPermissionStrategy(ProjectParticipantRepository projectParticipantRepository) {
        this.projectParticipantRepository = projectParticipantRepository;
    }

    @Override
    public boolean hasAccess(User user, Project project) {
        return projectParticipantRepository
                .findByProjectIdAndUserId(project.getId(), user.getId())
                .map(participant -> participant.getRole() == ParticipantRole.CREATOR)
                .orElse(false);
    }

    @Override
    public String getStrategyName() {
        return "CREATOR";
    }
}

