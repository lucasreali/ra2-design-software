package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.models.Project;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.repositories.ProjectRepository;
import dev.project.ra2avaliacao.repositories.UserRepository;
import dev.project.ra2avaliacao.strategies.PermissionStrategy;
import dev.project.ra2avaliacao.strategies.PermissionStrategyContext;
import org.springframework.stereotype.Component;

@Component
public class ProjectPermissionValidator {

    private final PermissionStrategyContext strategyContext;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectPermissionValidator(PermissionStrategyContext strategyFactory,
                                      ProjectRepository projectRepository,
                                      UserRepository userRepository) {
        this.strategyContext = strategyFactory;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public boolean hasAccess(String projectId, String userId, String requiredRole) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PermissionStrategy strategy = strategyContext.getStrategy(requiredRole);
        return strategy.hasAccess(user, project);
    }

    public void validateAccess(String projectId, String userId, String requiredRole) {
        if (!hasAccess(projectId, userId, requiredRole)) {
            throw new RuntimeException("User does not have permission to perform this action");
        }
    }

    public boolean isParticipant(String projectId, String userId) {
        return hasAccess(projectId, userId, "MEMBER");
    }

    public boolean isCreator(String projectId, String userId) {
        return hasAccess(projectId, userId, "CREATOR");
    }

    public boolean isAdmin(String projectId, String userId) {
        return hasAccess(projectId, userId, "ADMIN");
    }

    public boolean isMember(String projectId, String userId) {
        return hasAccess(projectId, userId, "MEMBER");
    }

    public boolean canManageProject(String projectId, String userId) {
        return hasAccess(projectId, userId, "ADMIN");
    }
}
