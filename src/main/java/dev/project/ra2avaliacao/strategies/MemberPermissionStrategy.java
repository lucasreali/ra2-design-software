package dev.project.ra2avaliacao.strategies;

import dev.project.ra2avaliacao.models.Project;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.repositories.ProjectParticipantRepository;
import org.springframework.stereotype.Component;

@Component
public class MemberPermissionStrategy implements PermissionStrategy {

    private final ProjectParticipantRepository projectParticipantRepository;

    public MemberPermissionStrategy(ProjectParticipantRepository projectParticipantRepository) {
        this.projectParticipantRepository = projectParticipantRepository;
    }

    @Override
    public boolean hasAccess(User user, Project project) {
        return projectParticipantRepository
                .findByProjectIdAndUserId(project.getId(), user.getId())
                .isPresent();
    }

    @Override
    public String getStrategyName() {
        return "MEMBER";
    }
}

