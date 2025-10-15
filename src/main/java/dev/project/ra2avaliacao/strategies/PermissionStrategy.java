package dev.project.ra2avaliacao.strategies;

import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.models.Project;

public interface PermissionStrategy {
    boolean hasAccess(User user, Project project);
    String getStrategyName();
}

