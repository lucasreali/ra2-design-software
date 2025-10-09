package dev.project.ra2avaliacao.repositories;

import dev.project.ra2avaliacao.models.ParticipantRole;
import dev.project.ra2avaliacao.models.ProjectParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, String> {
    List<ProjectParticipant> findByProjectId(String projectId);

    List<ProjectParticipant> findByUserId(String userId);

    Optional<ProjectParticipant> findByProjectIdAndUserId(String projectId, String userId);
}
